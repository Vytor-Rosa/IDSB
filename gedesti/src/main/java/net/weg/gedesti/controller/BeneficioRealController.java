package net.weg.gedesti.controller;

import lombok.AllArgsConstructor;
import net.weg.gedesti.dto.BeneficioRealDTO;
import net.weg.gedesti.model.entity.BeneficioReal;
import net.weg.gedesti.model.service.BeneficioRealService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
@RequestMapping("/api/beneficioreal")
public class BeneficioRealController {
    private BeneficioRealService beneficioRealService;

    @GetMapping
    public ResponseEntity<List<BeneficioReal>> findAll() {
        return ResponseEntity.status(HttpStatus.FOUND).body(beneficioRealService.findAll());
    }

    @PostMapping
    public ResponseEntity<Object> save(BeneficioRealDTO beneficioRealDTO) {
        BeneficioReal beneficioReal = new BeneficioReal();
        BeanUtils.copyProperties(beneficioRealDTO, beneficioReal);
        return ResponseEntity.status(HttpStatus.CREATED).body(beneficioRealService.save(beneficioReal));
    }

    public Optional<BeneficioReal> findById(Integer integer) {
        return beneficioRealService.findById(integer);
    }

    public boolean existsById(Integer integer) {
        return beneficioRealService.existsById(integer);
    }

    public void deleteById(Integer integer) {
        beneficioRealService.deleteById(integer);
    }
}
