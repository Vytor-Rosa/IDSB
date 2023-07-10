package net.weg.gedesti.controller;

import lombok.AllArgsConstructor;
import net.weg.gedesti.dto.DemandDTO;
import net.weg.gedesti.dto.SimilarityDTO;
import net.weg.gedesti.model.entity.Demand;
import net.weg.gedesti.model.entity.Similarity;
import net.weg.gedesti.model.service.DemandService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api/similarity")
@AllArgsConstructor
public class SimilarityController {

    DemandService DemandService;

    @PostMapping("/compare")
    public ResponseEntity<Object> compareTexts(@RequestBody @Valid SimilarityDTO similarityDTO) {

        Similarity similarity = new Similarity();
        BeanUtils.copyProperties(similarityDTO, similarity);

        List<Demand> demandOne = DemandService.findByDemandCode(similarity.getDemandOne().getDemandCode());
        List<Demand> demandTwo = DemandService.findByDemandCode(similarity.getDemandTwo().getDemandCode());
        Demand demandOneActive = new Demand();
        Demand demandTwoActive = new Demand();

        for(Demand demand : demandOne) {
            if(demand.getActiveVersion() == true){
                BeanUtils.copyProperties(demand, demandOneActive);
            }
        }

        for(Demand demand : demandTwo) {
            if(demand.getActiveVersion() == true){
                BeanUtils.copyProperties(demand, demandTwoActive);
            }
        }

        if (demandOne.isEmpty() || demandTwo.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error! No demand with code:  " + similarityDTO.getDemandOne().getDemandCode() + " or " + similarityDTO.getDemandTwo().getDemandCode());
        }

        String text1 = demandOneActive.getDemandObjective();
        String text2 = demandTwoActive.getDemandObjective();

        double similarityCalculate = calculateSimilarity(text1, text2);
        String sentiment = classifySimilarity(similarityCalculate);
        return ResponseEntity.status(HttpStatus.OK).body(sentiment);
    }

    private double calculateSimilarity(String text1, String text2) {
        // Pré-processamento
        text1 = preprocessText(text1);
        text2 = preprocessText(text2);

        // Criando o conjunto de tokens para cada texto
        Set<String> set1 = createTokenSet(text1);
        Set<String> set2 = createTokenSet(text2);

        // Calculando a similaridade de Jaccard
        double intersection = intersectionSize(set1, set2);
        double union = unionSize(set1, set2);
        double similarity = intersection / union;

        return similarity;
    }

    private String classifySimilarity(double similarity) {
        if (similarity > 0.5) {
            return "positivo";
        } else {
            return "negativo";
        }
    }

    private String preprocessText(String text) {
        text = text.replaceAll("[^a-zA-Z ]", "").toLowerCase();
        return text;
    }

    private Set<String> createTokenSet(String text) {
        String[] words = text.split("\\s+");
        Set<String> tokenSet = new HashSet<>();
        for (String word : words) {
            tokenSet.add(word);
        }
        return tokenSet;
    }

    private double intersectionSize(Set<String> set1, Set<String> set2) {
        Set<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);
        return intersection.size();
    }

    private double unionSize(Set<String> set1, Set<String> set2) {
        Set<String> union = new HashSet<>(set1);
        union.addAll(set2);
        return union.size();
    }
}

