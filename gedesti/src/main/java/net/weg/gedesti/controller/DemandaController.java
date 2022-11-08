package net.weg.gedesti.controller;

import lombok.AllArgsConstructor;
import net.weg.gedesti.dto.DemandaDTO;
import net.weg.gedesti.model.entity.CentroDemanda;
import net.weg.gedesti.model.entity.Demanda;
import net.weg.gedesti.model.service.CentroDemandaService;
import net.weg.gedesti.model.service.DemandaService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
@RequestMapping("/api/demanda")
public class DemandaController {
    private DemandaService demandaService;
    private CentroDemandaService centroDemandaService;

    @GetMapping
    public ResponseEntity<List<Demanda>> findAll() {
        return ResponseEntity.status(HttpStatus.FOUND).body(demandaService.findAll());
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid DemandaDTO demandaDTO) {
        Demanda demanda = new Demanda();
        BeanUtils.copyProperties(demandaDTO, demanda);
        Demanda demandaSalva = demandaService.save(demanda);

        for(CentroDemanda centroDemanda: demandaSalva.getCentroDeCusto()){
            centroDemanda.setDemanda(demandaSalva);
            centroDemandaService.save(centroDemanda);
        }


        return ResponseEntity.status(HttpStatus.CREATED).body(demandaSalva);
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Object> findById(@PathVariable(value = "codigo") Integer codigo) {
        Optional<Demanda> demandaOptional = demandaService.findById(codigo);
        if(demandaOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro! nenhuma demanda com o codigo: " + codigo);
        }

        List<Object> listaDemandas = new ArrayList<>();
        listaDemandas.add(demandaOptional.get());
        listaDemandas.add(centroDemandaService.findByDemanda(demandaOptional.get()));

        return ResponseEntity.status(HttpStatus.FOUND).body(listaDemandas);
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
