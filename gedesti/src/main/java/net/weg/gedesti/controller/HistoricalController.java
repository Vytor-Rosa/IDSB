package net.weg.gedesti.controller;

import lombok.AllArgsConstructor;
import net.weg.gedesti.dto.HistoricalDTO;
import net.weg.gedesti.model.entity.Demand;
import net.weg.gedesti.model.entity.Historical;
import net.weg.gedesti.model.service.DemandService;
import net.weg.gedesti.model.service.HistoricalService;
import net.weg.gedesti.util.HistoricalUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;


@RestController
@AllArgsConstructor
@RequestMapping("/api/historical")
public class HistoricalController {

    private HistoricalService historicalService;
    private DemandService demandService;

    @GetMapping
    public ResponseEntity<List<Historical>> findAll() {
        return ResponseEntity.status(HttpStatus.FOUND).body(historicalService.findAll());
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid HistoricalDTO historicalDTO) {
        Historical historical = new Historical();
        BeanUtils.copyProperties(historicalDTO, historical);
        Demand demand = demandService.findById(historical.getDemand().getDemandCode()).get();
        historical.setNewDemand(demand);
        System.out.println(historical.getNewDemand());
        return ResponseEntity.status(HttpStatus.CREATED).body(historicalService.save(historical));
    }

    @GetMapping("/{historicalCode}")
    public ResponseEntity<Object> findById(@PathVariable(value = "historicalCode") Integer historicalCode) {
        Optional<Historical> historicalOptional = historicalService.findById(historicalCode);
        if (historicalOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error! no historical with code: " + historicalCode);
        }
        return ResponseEntity.status(HttpStatus.FOUND).body(historicalOptional);
    }

    @DeleteMapping("/{historicalCode}")
    public ResponseEntity<Object> deleteById(@PathVariable(value = "historicalCode") Integer historicalCode) {
        Optional<Historical> historicalOptional = historicalService.findById(historicalCode);
        if (historicalOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error! no historical with code: " + historicalCode);
        }
        historicalService.deleteById(historicalCode);
        return ResponseEntity.status(HttpStatus.FOUND).body(historicalOptional);
    }
}
