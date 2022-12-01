package net.weg.gedesti.controller;

import lombok.AllArgsConstructor;
import net.weg.gedesti.model.entity.Historical;
import net.weg.gedesti.model.service.HistoricalService;
import net.weg.gedesti.util.HistoricoUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;


@Controller
@AllArgsConstructor
@RequestMapping("/api/historical")
public class HistoricalController {

    private HistoricalService historicoService;

    @GetMapping
    public ResponseEntity<List<Historical>> findAll() {
        return ResponseEntity.status(HttpStatus.FOUND).body(historicoService.findAll());
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestParam(value = "historico") @Valid String historicoJson, @RequestParam(value = "anexoHistorico") MultipartFile anexoHistorico) {
        HistoricoUtil historicoUtil = new HistoricoUtil();
        Historical historico = historicoUtil.convertJsonToModel(historicoJson);
        historico.setAnexo(anexoHistorico);
        return ResponseEntity.status(HttpStatus.CREATED).body(historicoService.save(historico));
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Object> findById(@PathVariable(value = "codigo") Integer codigo) {
        Optional<Historical> historicoOptional = historicoService.findById(codigo);
        if(historicoOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro! Nenhum histórico com o codigo: " + codigo);
        }
        return ResponseEntity.status(HttpStatus.FOUND).body(historicoOptional);
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<Object> deleteById(@PathVariable(value = "codigo") Integer codigo) {
        Optional<Historical> historicoOptional = historicoService.findById(codigo);
        if(historicoOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro! Nenhum histórico com o codigo: " + codigo);
        }
        historicoService.deleteById(codigo);
        return ResponseEntity.status(HttpStatus.FOUND).body(historicoOptional);
    }
}
