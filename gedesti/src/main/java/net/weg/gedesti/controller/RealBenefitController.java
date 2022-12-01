package net.weg.gedesti.controller;

import lombok.AllArgsConstructor;
import net.weg.gedesti.dto.RealBenefitDTO;
import net.weg.gedesti.model.entity.BeneficioReal;
import net.weg.gedesti.model.service.RealBenefitService;
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
@RequestMapping("/api/realbenefit")
public class RealBenefitController {
    private RealBenefitService beneficioRealService;

    @GetMapping
    public ResponseEntity<List<BeneficioReal>> findAll() {
        return ResponseEntity.status(HttpStatus.FOUND).body(beneficioRealService.findAll());
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid RealBenefitDTO beneficioRealDTO) {
        BeneficioReal beneficioReal = new BeneficioReal();
        BeanUtils.copyProperties(beneficioRealDTO, beneficioReal);
        return ResponseEntity.status(HttpStatus.CREATED).body(beneficioRealService.save(beneficioReal));
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Object> findById(@PathVariable(value = "codigo") Integer codigo) {
        Optional<BeneficioReal> beneficioRealOptional = beneficioRealService.findById(codigo);
        if(beneficioRealOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro! Nenhum benefecio real com codigo: " + codigo);
        }
        return ResponseEntity.status(HttpStatus.FOUND).body(beneficioRealOptional);
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<Object> deleteById(@PathVariable(value = "codigo") Integer codigo) {
        Optional<BeneficioReal> beneficioRealOptional = beneficioRealService.findById(codigo);
        if(beneficioRealOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro! Nenhum benefecio real com codigo: " + codigo);
        }
        beneficioRealService.deleteById(codigo);
        return ResponseEntity.status(HttpStatus.OK).body("Beneficio real " + codigo + " deletado com sucesso!");
    }
}
