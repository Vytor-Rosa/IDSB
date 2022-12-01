package net.weg.gedesti.controller;

import lombok.AllArgsConstructor;
import net.weg.gedesti.dto.PotentialBenefitDTO;
import net.weg.gedesti.model.entity.PotentialBenefit;
import net.weg.gedesti.model.service.PotentialBenefitService;
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
@RequestMapping("/api/potencialbenefit")
public class PotentialBenefitController {

    private PotentialBenefitService beneficioPotencialService;

    @GetMapping
    public ResponseEntity<List<PotentialBenefit>> findAll() {
        return ResponseEntity.status(HttpStatus.FOUND).body(beneficioPotencialService.findAll());
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid PotentialBenefitDTO beneficioPotencialDTO) {
        PotentialBenefit beneficioPotencial = new PotentialBenefit();
        BeanUtils.copyProperties(beneficioPotencialDTO, beneficioPotencial);
        return ResponseEntity.status(HttpStatus.CREATED).body(beneficioPotencialService.save(beneficioPotencial));
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Object> findById(@PathVariable(value = "codigo") Integer codigo) {
        Optional<PotentialBenefit> beneficioPotencialOptional = beneficioPotencialService.findById(codigo);
        if(beneficioPotencialOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro! Nenhum beneficio potencial com o codigo: " + codigo);
        }
        return ResponseEntity.status(HttpStatus.FOUND).body(beneficioPotencialOptional);
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<Object> deleteById(@PathVariable(value = "codigo") Integer codigo) {
        Optional<PotentialBenefit> beneficioPotencialOptional = beneficioPotencialService.findById(codigo);
        if(beneficioPotencialOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro! Nenhum beneficio potencial com o codigo: " + codigo);
        }
        beneficioPotencialService.deleteById(codigo);
        return ResponseEntity.status(HttpStatus.FOUND).body("Beneficio potencial " + codigo + " deletado com sucesso!");
    }
}
