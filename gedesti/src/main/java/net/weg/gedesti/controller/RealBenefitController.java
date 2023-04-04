package net.weg.gedesti.controller;

import lombok.AllArgsConstructor;
import net.weg.gedesti.dto.ClassificationDTO;
import net.weg.gedesti.dto.RealBenefitDTO;
import net.weg.gedesti.model.entity.RealBenefit;
import net.weg.gedesti.model.service.RealBenefitService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api/realbenefit")
public class RealBenefitController {
    private RealBenefitService realBenefitService;

    @GetMapping
    public ResponseEntity<List<RealBenefit>> findAll() {
        return ResponseEntity.status(HttpStatus.FOUND).body(realBenefitService.findAll());
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid RealBenefitDTO realBenefitDTO) {
        RealBenefit realBenefit = new RealBenefit();
        BeanUtils.copyProperties(realBenefitDTO, realBenefit);
        return ResponseEntity.status(HttpStatus.CREATED).body(realBenefitService.save(realBenefit));
    }

    @GetMapping("/{realBenefitCode}")
    public ResponseEntity<Object> findById(@PathVariable(value = "realBenefitCode") Integer realBenefitCode) {
        Optional<RealBenefit> realBenefitOptional = realBenefitService.findById(realBenefitCode);
        if (realBenefitOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error! No real benefit with code:  " + realBenefitCode);
        }
        return ResponseEntity.status(HttpStatus.FOUND).body(realBenefitOptional);
    }

    @DeleteMapping("/{realBenefitCode}")
    public ResponseEntity<Object> deleteById(@PathVariable(value = "realBenefitCode") Integer realBenefitCode) {
        Optional<RealBenefit> realBenefitOptional = realBenefitService.findById(realBenefitCode);
        if (realBenefitOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error! No real benefit with code:  " + realBenefitCode);
        }
        realBenefitService.deleteById(realBenefitCode);
        return ResponseEntity.status(HttpStatus.OK).body("Real Benefit " + realBenefitCode + " successfully deleted!");
    }

    @Modifying
    @Transactional
    @PutMapping("/{realBenefitCode}")
    public ResponseEntity<Object> update(@PathVariable(value = "realBenefitCode") Integer realBenefitCode, @RequestBody @Valid RealBenefitDTO realBenefitDTO) {
        Optional<RealBenefit> realBenefitOptional = realBenefitService.findById(realBenefitCode);
        if (realBenefitOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error! No real benefit with code:  " + realBenefitCode);
        }
        RealBenefit realBenefit = new RealBenefit();
        BeanUtils.copyProperties(realBenefitDTO, realBenefit);
        realBenefit.setRealBenefitCode(realBenefitCode);
        return ResponseEntity.status(HttpStatus.OK).body(realBenefitService.save(realBenefit));
    }

}
