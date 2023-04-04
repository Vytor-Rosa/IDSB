package net.weg.gedesti.controller;

import lombok.AllArgsConstructor;
import lombok.Value;
import net.weg.gedesti.dto.ReproachDTO;
import net.weg.gedesti.model.entity.Reproach;
import net.weg.gedesti.model.service.ReproachService;
import net.weg.gedesti.repository.RealBenefitRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api/reproach")
public class ReproachController {
    private ReproachService reproachService;

    @GetMapping
    public ResponseEntity<List<Reproach>> findAll() {
        return ResponseEntity.status(HttpStatus.FOUND).body(reproachService.findAll());
    }

    @GetMapping("/{reproachCode}")
    public ResponseEntity<Object> findById(@PathVariable(value = "reproachCode") Integer reproachCode) {
        Optional<Reproach> reproachOptional = reproachService.findById(reproachCode);
        if (reproachOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error! No real benefit with code:  " + reproachCode);
        }
        return ResponseEntity.status(HttpStatus.FOUND).body(reproachService.findById(reproachCode));
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid ReproachDTO reproachDTO) {
        Reproach reproach = new Reproach();
        BeanUtils.copyProperties(reproachDTO, reproach);
        return ResponseEntity.status(HttpStatus.OK).body(reproachService.save(reproach));
    }
}
