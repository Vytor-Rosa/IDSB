package net.weg.gedesti.controller;

import lombok.AllArgsConstructor;
import net.weg.gedesti.dto.BeneficioQualitativoDTO;
import net.weg.gedesti.model.entity.BeneficioQualitativo;
import net.weg.gedesti.model.service.BeneficioQualitativoService;
import net.weg.gedesti.repository.BeneficioQualitativoRepository;
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
@RequestMapping("/api/qualitativebenefit")
public class BeneficioQualitativoController {

    private BeneficioQualitativoService beneficioQualitativoService;

    @GetMapping
    public ResponseEntity<List<BeneficioQualitativo>> findAll() {
        return ResponseEntity.status(HttpStatus.FOUND).body(beneficioQualitativoService.findAll());
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid BeneficioQualitativoDTO beneficioQualitativoDTO) {
        BeneficioQualitativo beneficioQualitativo = new BeneficioQualitativo();
        BeanUtils.copyProperties(beneficioQualitativoDTO, beneficioQualitativo);
        return ResponseEntity.status(HttpStatus.CREATED).body(beneficioQualitativoService.save(beneficioQualitativo));
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Object> findById(@PathVariable(value = "codigo") Integer codigo) {
        Optional<BeneficioQualitativo> beneficioQualitativoOptional = beneficioQualitativoService.findById(codigo);
        if(beneficioQualitativoOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro! Nenhum beneficio qualitativo com o codigo:" + codigo);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(beneficioQualitativoOptional);
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<Object> deleteById(@PathVariable(value = "codigo") Integer codigo) {
        Optional<BeneficioQualitativo> beneficioQualitativoOptional = beneficioQualitativoService.findById(codigo);
        if(beneficioQualitativoOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro! Nenhum beneficio qualitativo com o codigo: " + codigo);
        }
        beneficioQualitativoService.deleteById(codigo);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Beneficio Qualitativo " + codigo + " deletado!");
    }
}
