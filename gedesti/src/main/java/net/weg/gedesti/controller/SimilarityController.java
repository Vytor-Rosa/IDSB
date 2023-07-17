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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/similarity")
@AllArgsConstructor
public class SimilarityController {

    DemandService demandService;

    @PostMapping("/compare/{demandCode}")
    public ResponseEntity<Object> compareTexts(@PathVariable(value = "demandCode") Integer demandCode) {

        List<Demand> demandOptional = demandService.findByDemandCode(demandCode);
        Demand demandCompare = null;

        for (Demand demands : demandOptional) {
            if (demands.getActiveVersion()) {
                demandCompare = demands;
                break;
            }
        }

        if (demandCompare == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error! No active demand with code: " + demandCode);
        }

        List<Demand> demands = demandService.findAll();

        if (demands.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error! No demands found.");
        }

        Demand mostSimilarDemand = null;
        double maxSimilarity = Double.MIN_VALUE;
        String textToCompare = demandCompare.getDemandTitle();

        for (Demand demand : demands) {
            if (demand.getDemandCode() != demandCompare.getDemandCode() && !demand.getDemandStatus().equals("Cancelled")) {
                String currentText = demand.getDemandTitle();
                double currentSimilarity = calculateSimilarity(textToCompare, currentText);

                if (currentSimilarity > maxSimilarity) {
                    maxSimilarity = currentSimilarity;
                    mostSimilarDemand = demand;
                }
            }
        }

        if (mostSimilarDemand == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error! No similar demand found.");
        }

        double similarityPercentage = maxSimilarity * 100;

        Similarity similarity = new Similarity();
        similarity.setDemandOne(demandCompare);
        similarity.setDemandTwo(mostSimilarDemand);
        similarity.setSimilarityValue(similarityPercentage);

        return ResponseEntity.status(HttpStatus.OK).body(similarity);
    }

    private double calculateSimilarity(String text1, String text2) {
        text1 = preprocessText(text1);
        text2 = preprocessText(text2);

        Set<String> set1 = createTokenSet(text1);
        Set<String> set2 = createTokenSet(text2);

        double intersection = intersectionSize(set1, set2);
        double union = unionSize(set1, set2);
        double similarity = intersection / union;

        return similarity;
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
