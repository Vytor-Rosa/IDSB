package net.weg.gedesti.controller;

import lombok.AllArgsConstructor;
import net.weg.gedesti.dto.ColorsDTO;
import net.weg.gedesti.dto.CostCenterDTO;
import net.weg.gedesti.model.entity.Colors;
import net.weg.gedesti.model.entity.CostCenter;
import net.weg.gedesti.model.service.ColorsService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api/colors")
public class ColorsController {

    private ColorsService colorsService;


    @Modifying
    @Transactional
    @PutMapping("/{colorsCode}")
    public ResponseEntity<Object> update(@PathVariable(value = "colorsCode") Integer colorsCode, @RequestBody @Valid ColorsDTO colorsDTO) {
        Optional<Colors> costCenterOptional = colorsService.findById(colorsCode);
        if(costCenterOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error! No cost center with code: " + colorsCode);
        }
        Colors colors = costCenterOptional.get();
        BeanUtils.copyProperties(colorsDTO, colors);
        return ResponseEntity.status(HttpStatus.CREATED).body(colorsService.save(colors));
    }
}
