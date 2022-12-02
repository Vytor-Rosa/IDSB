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

    private PotentialBenefitService potentialBenefitService;

    @GetMapping
    public ResponseEntity<List<PotentialBenefit>> findAll() {
        return ResponseEntity.status(HttpStatus.FOUND).body(potentialBenefitService.findAll());
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid PotentialBenefitDTO potentialBenefitDTO) {
        PotentialBenefit potentialBenefit = new PotentialBenefit();
        BeanUtils.copyProperties(potentialBenefitDTO, potentialBenefit);
        return ResponseEntity.status(HttpStatus.CREATED).body(potentialBenefitService.save(potentialBenefit));
    }

    @GetMapping("/{potentialBenefitCode}")
    public ResponseEntity<Object> findById(@PathVariable(value = "potentialBenefitCode") Integer potentialBenefitCode) {
        Optional<PotentialBenefit> potentialBenefitOptional = potentialBenefitService.findById(potentialBenefitCode);
        if(potentialBenefitOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error! No potential benefitC with code:" + potentialBenefitCode);
        }
        return ResponseEntity.status(HttpStatus.FOUND).body(potentialBenefitOptional);
    }

    @DeleteMapping("/{potentialBenefitCode}")
    public ResponseEntity<Object> deleteById(@PathVariable(value = "potentialBenefitCode") Integer potentialBenefitCode) {
        Optional<PotentialBenefit> potentialBenefitOptional = potentialBenefitService.findById(potentialBenefitCode);
        if(potentialBenefitOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error! No potential benefit with code:" + potentialBenefitCode);
        }
        potentialBenefitService.deleteById(potentialBenefitCode);
        return ResponseEntity.status(HttpStatus.FOUND).body("Potential Benefit " + potentialBenefitCode + " successfully deleted!");
    }
}
