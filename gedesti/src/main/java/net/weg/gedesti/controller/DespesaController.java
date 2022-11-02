package net.weg.gedesti.controller;

import lombok.AllArgsConstructor;
import net.weg.gedesti.dto.DespesaDTO;
import net.weg.gedesti.model.entity.Despesa;
import net.weg.gedesti.model.service.DespesaService;
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
@RequestMapping("/api/despesa")
public class DespesaController {

    private DespesaService despesaService;

    @GetMapping
    public ResponseEntity<List<Despesa>> findAll() {
        return ResponseEntity.status(HttpStatus.FOUND).body(despesaService.findAll());
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid DespesaDTO despesaDTO) {
        Despesa despesa = new Despesa();
        BeanUtils.copyProperties(despesaDTO, despesa);
        return ResponseEntity.status(HttpStatus.FOUND).body(despesaService.save(despesa));
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Object> findById(@PathVariable(value = "codigo") Integer codigo) {
        Optional<Despesa> despesaOptional = despesaService.findById(codigo);
        if (despesaOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro! nenhuma despesa com o codigo: " + codigo);
        }
        return ResponseEntity.status(HttpStatus.FOUND).body(despesaOptional);
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<Object> deleteById(@PathVariable(value = "codigo") Integer codigo) {
        Optional<Despesa> despesaOptional = despesaService.findById(codigo);
        if (despesaOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro! nenhuma despesa com o codigo: " + codigo);
        }
        despesaService.deleteById(codigo);
        return ResponseEntity.status(HttpStatus.FOUND).body("Despesa " + codigo + " deletada com sucesso!");
    }
}
