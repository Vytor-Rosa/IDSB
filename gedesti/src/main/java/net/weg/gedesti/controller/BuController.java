package net.weg.gedesti.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.weg.gedesti.dto.BuDTO;
import net.weg.gedesti.model.entity.Bu;
import net.weg.gedesti.model.service.BuService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Entity;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
@RequestMapping("/api/bu")
public class BuController {

    private BuService buService;

    @GetMapping
    public ResponseEntity<List<Bu>> findAll() {
        return ResponseEntity.status(HttpStatus.FOUND).body(buService.findAll());
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid BuDTO buDTO) {
        Bu bu = new Bu();
        BeanUtils.copyProperties(buDTO, bu);
        return ResponseEntity.status(HttpStatus.CREATED).body(buService.save(bu));
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Object> findById(@PathVariable(value = "codigo") Integer codigo) {
        Optional<Bu> buOptional = buService.findById(codigo);
        if(buOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro! Nenhuma bu com o codigo: " + codigo);
        }
        return ResponseEntity.status(HttpStatus.FOUND).body(buOptional);
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<Object> deleteById(@PathVariable(value = "codigo") Integer codigo) {
        Optional<Bu> buOptional = buService.findById(codigo);
        if(buOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro! Nenhuma bu com o codigo: " + codigo);
        }
        buService.deleteById(codigo);
        return ResponseEntity.status(HttpStatus.FOUND).body(buOptional);
    }
}
