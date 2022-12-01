package net.weg.gedesti.controller;

import lombok.AllArgsConstructor;
import net.weg.gedesti.dto.CostCenterDTO;
import net.weg.gedesti.model.entity.CentroDeCusto;
import net.weg.gedesti.model.service.CostCenterService;
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
@RequestMapping("/api/costcenter")
public class CentroDeCustoController {
    private CostCenterService centroDeCustoService;

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid CostCenterDTO centroDeCustoDTO) {
        CentroDeCusto centroDeCusto = new CentroDeCusto();
        BeanUtils.copyProperties(centroDeCustoDTO, centroDeCusto);
        return ResponseEntity.status(HttpStatus.CREATED).body(centroDeCustoService.save(centroDeCusto));
    }

    @GetMapping
    public ResponseEntity<List<CentroDeCusto>> findAll() {
        return ResponseEntity.status(HttpStatus.FOUND).body(centroDeCustoService.findAll());
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Object> findById(@PathVariable(value = "codigo") Integer codigo) {
        Optional<CentroDeCusto> centroDeCustoOptional = centroDeCustoService.findById(codigo);
        if(centroDeCustoOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro! Nenhum centro de custo com codigo: " + codigo);
        }
        return ResponseEntity.status(HttpStatus.FOUND).body(centroDeCustoOptional);
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<Object> deleteById(@PathVariable(value = "codigo") Integer codigo) {
        Optional<CentroDeCusto> centroDeCustoOptional = centroDeCustoService.findById(codigo);
        if(centroDeCustoOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro! Nenhum centro de custo com codigo: " + codigo);
        }
        centroDeCustoService.deleteById(codigo);
        return ResponseEntity.status(HttpStatus.FOUND).body("Centro de custo " + codigo + " deletado com sucesso");
    }
}
