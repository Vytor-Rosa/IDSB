package net.weg.gedesti.controller;

import lombok.AllArgsConstructor;
import net.weg.gedesti.dto.CostCenterDTO;
import net.weg.gedesti.model.entity.CostCenter;
import net.weg.gedesti.model.service.CostCenterService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api/costcenter")
public class CostCenterController {
    private CostCenterService costCenterService;

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid CostCenterDTO costCenterDTO) {
        CostCenter costCenter = new CostCenter();
        BeanUtils.copyProperties(costCenterDTO, costCenter);
        return ResponseEntity.status(HttpStatus.CREATED).body(costCenterService.save(costCenter));
    }

    @GetMapping
    public ResponseEntity<List<CostCenter>> findAll() {
        return ResponseEntity.status(HttpStatus.FOUND).body(costCenterService.findAll());
    }

    @GetMapping("/{CostCenterCode}")
    public ResponseEntity<Object> findById(@PathVariable(value = "CostCenterCode") Integer CostCenterCode) {
        Optional<CostCenter> costCenterOptional = costCenterService.findById(CostCenterCode);
        if(costCenterOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error! No cost center with code: " + CostCenterCode);
        }
        return ResponseEntity.status(HttpStatus.FOUND).body(costCenterOptional);
    }


    @GetMapping("/name/{CostCenter}")
    public ResponseEntity<Object> findByCostCenter(@PathVariable (value = "CostCenter") String CostCenter){
        Optional<CostCenter> costCenterOptional = costCenterService.findByCostCenter(CostCenter);

        if(costCenterOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error! No cost center with name: " + CostCenter);
        }

        return ResponseEntity.status(HttpStatus.FOUND).body(costCenterOptional);
    }

    @DeleteMapping("/{CostCenterCode}")
    public ResponseEntity<Object> deleteById(@PathVariable(value = "CostCenterCode") Integer CostCenterCode) {
        Optional<CostCenter> costCenterOptional = costCenterService.findById(CostCenterCode);
        if(costCenterOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error! No cost center with code: " + CostCenterCode);
        }
        costCenterService.deleteById(CostCenterCode);
        return ResponseEntity.status(HttpStatus.FOUND).body("Cost Center " + CostCenterCode + " successfully deleted!");
    }

    @Modifying
    @Transactional
    @PutMapping("/{CostCenterCode}")
    public ResponseEntity<Object> update(@PathVariable(value = "CostCenterCode") Integer CostCenterCode, @RequestBody @Valid CostCenterDTO costCenterDTO) {
        Optional<CostCenter> costCenterOptional = costCenterService.findById(CostCenterCode);
        if(costCenterOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error! No cost center with code: " + CostCenterCode);
        }
        CostCenter costCenter = costCenterOptional.get();
        BeanUtils.copyProperties(costCenterDTO, costCenter);
        return ResponseEntity.status(HttpStatus.CREATED).body(costCenterService.save(costCenter));
    }


}
