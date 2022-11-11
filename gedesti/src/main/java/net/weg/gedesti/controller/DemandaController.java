package net.weg.gedesti.controller;

import lombok.AllArgsConstructor;
import net.weg.gedesti.dto.DemandaDTO;
import net.weg.gedesti.model.entity.Demanda;
import net.weg.gedesti.model.service.DemandaService;
import net.weg.gedesti.util.DemandaUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
@RequestMapping("/api/demanda")
public class DemandaController {
    private DemandaService demandaService;

    @GetMapping
    public ResponseEntity<List<Demanda>> findAll() {
        return ResponseEntity.status(HttpStatus.FOUND).body(demandaService.findAll());
    }

    @GetMapping("/page")
    public ResponseEntity<Page<Demanda>> findAll(@PageableDefault(page = 9, size = 8, direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.FOUND).body(demandaService.findAll(pageable));
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestParam(value = "demanda") @Valid String demandaJson, @RequestParam(value = "anexoDemanda") MultipartFile anexoDemanda) {
        DemandaUtil demandaUtil = new DemandaUtil();
        Demanda demanda = demandaUtil.convertJsonToModel(demandaJson);
        demanda.setAnexoDemanda(anexoDemanda);
        return ResponseEntity.status(HttpStatus.CREATED).body(demandaService.save(demanda));
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Object> findById(@PathVariable(value = "codigo") Integer codigo) {
        Optional<Demanda> demandaOptional = demandaService.findById(codigo);
        if(demandaOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro! nenhuma demanda com o codigo: " + codigo);
        }
//
//        List<Object> listaDemandas = new ArrayList<>();
//        listaDemandas.add(demandaOptional.get());
//        listaDemandas.add(centroDemandaService.findByDemanda(demandaOptional.get()));

        return ResponseEntity.status(HttpStatus.FOUND).body(demandaOptional);
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<Object> deleteById(@PathVariable(value = "codigo") Integer codigo) {
        Optional<Demanda> demandaOptional = demandaService.findById(codigo);
        if(demandaOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro! nenhuma demanda com o codigo: " + codigo);
        }
        demandaService.deleteById(codigo);
        return ResponseEntity.status(HttpStatus.FOUND).body("Demanda " + codigo + " deletada com sucesso!");
    }
}
