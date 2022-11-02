package net.weg.gedesti.controller;

import lombok.AllArgsConstructor;
import net.weg.gedesti.dto.CentroDeCustoDTO;
import net.weg.gedesti.model.entity.CentroDeCusto;
import net.weg.gedesti.model.service.CentroDeCustoService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
@RequestMapping("/api/centrodecusto")
public class CentroDeCustoController {
    private CentroDeCustoService centroDeCustoService;

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid CentroDeCustoDTO centroDeCustoDTO) {
        CentroDeCusto centroDeCusto = new CentroDeCusto();
        BeanUtils.copyProperties(centroDeCustoDTO, centroDeCusto);
        return ResponseEntity.status(HttpStatus.CREATED).body(centroDeCustoService.save(centroDeCusto));
    }

    @GetMapping
    public ResponseEntity<List<CentroDeCusto>> findAll() {
        return ResponseEntity.status(HttpStatus.FOUND).body(centroDeCustoService.findAll());
    }

    public Optional<CentroDeCusto> findById(Integer integer) {
        return centroDeCustoService.findById(integer);
    }

    public void deleteById(Integer integer) {
        centroDeCustoService.deleteById(integer);
    }
}
