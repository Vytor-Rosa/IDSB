package net.weg.gedesti.controller;

import lombok.AllArgsConstructor;
import net.weg.gedesti.dto.ClassificationDTO;
import net.weg.gedesti.model.entity.Classificacao;
import net.weg.gedesti.model.service.ClassificationService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;


@Controller
@AllArgsConstructor
@RequestMapping("/api/classification")
public class ClassificationController {

    ClassificationService classificacaoService;


    @GetMapping
    public ResponseEntity<List<Classificacao>> findAll() {
        return ResponseEntity.status(HttpStatus.FOUND).body(classificacaoService.findAll());
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid ClassificationDTO classificacaoDTO) {
        Classificacao classificacao  = new Classificacao();
        BeanUtils.copyProperties(classificacaoDTO, classificacao);
        return ResponseEntity.status(HttpStatus.CREATED).body(classificacaoService.save(classificacao));
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Object> findById(@PathVariable(value = "codigo") Integer codigo) {
        Optional<Classificacao> classificacaoOptional = classificacaoService.findById(codigo);
        if(classificacaoOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro! nenhuma classificação com o codigo: " + codigo);
        }

        return ResponseEntity.status(HttpStatus.FOUND).body(classificacaoOptional);
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<Object> deleteById(@PathVariable(value = "codigo") Integer codigo) {
        Optional<Classificacao> classificacaoOptional = classificacaoService.findById(codigo);
        if(classificacaoOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro! nenhuma classificação com o codigo: " + codigo);
        }
        classificacaoService.deleteById(codigo);
        return ResponseEntity.status(HttpStatus.FOUND).body("Classificação " + codigo + " deletada com sucesso!");
    }
}
