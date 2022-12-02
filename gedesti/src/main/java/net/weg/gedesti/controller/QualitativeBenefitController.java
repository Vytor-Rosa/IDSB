package net.weg.gedesti.controller;

import lombok.AllArgsConstructor;
import net.weg.gedesti.dto.QualitativeBenefitDTO;
import net.weg.gedesti.model.entity.QualitativeBenefit;
import net.weg.gedesti.model.service.QualitativeBenefitService;
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
public class QualitativeBenefitController {

    private QualitativeBenefitService qualitativeBenefitService;

    @GetMapping
    public ResponseEntity<List<QualitativeBenefit>> findAll() {
        return ResponseEntity.status(HttpStatus.FOUND).body(qualitativeBenefitService.findAll());
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid QualitativeBenefitDTO qualitativeBenefitDTO) {
        QualitativeBenefit qualitativeBenefit = new QualitativeBenefit();
        BeanUtils.copyProperties(qualitativeBenefitDTO, qualitativeBenefit);
        return ResponseEntity.status(HttpStatus.CREATED).body(qualitativeBenefitService.save(qualitativeBenefit));
    }

    @GetMapping("/{qualitativeBenefitCode}")
    public ResponseEntity<Object> findById(@PathVariable(value = "qualitativeBenefitCode") Integer qualitativeBenefitCode) {
        Optional<QualitativeBenefit> qualitativeBenefitOptional = qualitativeBenefitService.findById(qualitativeBenefitCode);
        if(qualitativeBenefitOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error! No qualitative Benefit with code:" + qualitativeBenefitCode);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(qualitativeBenefitOptional);
    }

    @DeleteMapping("/{qualitativeBenefitCode}")
    public ResponseEntity<Object> deleteById(@PathVariable(value = "qualitativeBenefitCode") Integer qualitativeBenefitCode) {
        Optional<QualitativeBenefit> qualitativeBenefitOptional = qualitativeBenefitService.findById(qualitativeBenefitCode);
        if(qualitativeBenefitOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error! No qualitative Benefit with code: " + qualitativeBenefitCode);
        }
        qualitativeBenefitService.deleteById(qualitativeBenefitCode);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Qualitative BenefitCode " + qualitativeBenefitCode + " successfully deleted!");
    }
}
