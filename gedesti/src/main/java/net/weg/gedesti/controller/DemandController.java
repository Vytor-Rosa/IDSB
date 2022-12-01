package net.weg.gedesti.controller;

import lombok.AllArgsConstructor;
import net.weg.gedesti.model.entity.Demand;
import net.weg.gedesti.model.service.DemandService;
import net.weg.gedesti.util.DemandaUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
@RequestMapping("/api/demand")
public class DemandController {
    private DemandService demandaService;

    @GetMapping
    public ResponseEntity<List<Demand>> findAll() {
        return ResponseEntity.status(HttpStatus.FOUND).body(demandaService.findAll());
    }

    @GetMapping("/page")
    public ResponseEntity<Page<Demand>> findAll(@PageableDefault(page = 9, size = 8, direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.FOUND).body(demandaService.findAll(pageable));
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestParam(value = "demanda") @Valid String demandaJson, @RequestParam(value = "anexoDemanda") MultipartFile anexoDemanda) {
        DemandaUtil demandaUtil = new DemandaUtil();
        Demand demanda = demandaUtil.convertJsonToModel(demandaJson);
        demanda.setDemandAttachment(anexoDemanda);
        return ResponseEntity.status(HttpStatus.CREATED).body(demandaService.save(demanda));
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Object> findById(@PathVariable(value = "codigo") Integer codigo) {
        Optional<Demand> demandaOptional = demandaService.findById(codigo);
        if(demandaOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro! nenhuma demanda com o codigo: " + codigo);
        }

        return ResponseEntity.status(HttpStatus.FOUND).body(demandaOptional);
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<Object> deleteById(@PathVariable(value = "codigo") Integer codigo) {
        Optional<Demand> demandaOptional = demandaService.findById(codigo);
        if(demandaOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro! nenhuma demanda com o codigo: " + codigo);
        }
        demandaService.deleteById(codigo);
        return ResponseEntity.status(HttpStatus.FOUND).body("Demanda " + codigo + " deletada com sucesso!");
    }

    @Modifying
    @Transactional
    @PutMapping("/{codigo}")
    public ResponseEntity<Object> update(@RequestParam(value = "demanda") @Valid String demandaJson,
                                         @RequestParam(value = "anexoDemanda") MultipartFile anexoDemanda,
                                         @PathVariable(value = "codigo") Integer codigo) {
        if(!demandaService.existsById(codigo)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não existe");
        }

        DemandaUtil demandaUtil = new DemandaUtil();
        Demand demanda = demandaUtil.convertJsonToModel(demandaJson);
        demanda.setDemandAttachment(anexoDemanda);
        demanda.setDemandCode(codigo);

        return ResponseEntity.status(HttpStatus.CREATED).body(demandaService.save(demanda));
    }

}
