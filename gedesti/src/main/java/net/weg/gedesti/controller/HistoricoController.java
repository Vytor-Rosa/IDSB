package net.weg.gedesti.controller;

import lombok.AllArgsConstructor;
import net.weg.gedesti.dto.BuDTO;
import net.weg.gedesti.dto.HistoricoDTO;
import net.weg.gedesti.model.entity.Bu;
import net.weg.gedesti.model.entity.Historico;
import net.weg.gedesti.model.service.BuService;
import net.weg.gedesti.model.service.HistoricoService;
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
@RequestMapping("/api/historico")
public class HistoricoController {

    private HistoricoService historicoService;

    @GetMapping
    public ResponseEntity<List<Historico>> findAll() {
        return ResponseEntity.status(HttpStatus.FOUND).body(historicoService.findAll());
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid HistoricoDTO historicoDTO) {
        Historico historico = new Historico();
        BeanUtils.copyProperties(historicoDTO, historico);
        return ResponseEntity.status(HttpStatus.CREATED).body(historicoService.save(historico));
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Object> findById(@PathVariable(value = "codigo") Integer codigo) {
        Optional<Historico> historicoOptional = historicoService.findById(codigo);
        if(historicoOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro! Nenhum histórico com o codigo: " + codigo);
        }
        return ResponseEntity.status(HttpStatus.FOUND).body(historicoOptional);
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<Object> deleteById(@PathVariable(value = "codigo") Integer codigo) {
        Optional<Historico> historicoOptional = historicoService.findById(codigo);
        if(historicoOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro! Nenhum histórico com o codigo: " + codigo);
        }
        historicoService.deleteById(codigo);
        return ResponseEntity.status(HttpStatus.FOUND).body(historicoOptional);
    }
}
