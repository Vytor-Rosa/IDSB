package net.weg.gedesti.controller;

import lombok.AllArgsConstructor;
import net.weg.gedesti.dto.ClassificationDTO;
import net.weg.gedesti.model.entity.Classification;
import net.weg.gedesti.model.service.ClassificationService;
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
@RequestMapping("/api/classification")
public class ClassificationController {

    ClassificationService classificationService;


    @GetMapping
    public ResponseEntity<List<Classification>> findAll() {
        return ResponseEntity.status(HttpStatus.FOUND).body(classificationService.findAll());
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid ClassificationDTO classificationDTO) {
        Classification classification  = new Classification();
        BeanUtils.copyProperties(classificationDTO, classification);
        return ResponseEntity.status(HttpStatus.CREATED).body(classificationService.save(classification));
    }

    @GetMapping("/{classificationCode}")
    public ResponseEntity<Object> findById(@PathVariable(value = "classificationCode") Integer classificationCode) {
        Optional<Classification> classificationOptional = classificationService.findById(classificationCode);
        if(classificationOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error! no classification with code: " + classificationCode);
        }

        return ResponseEntity.status(HttpStatus.FOUND).body(classificationOptional);
    }

    @DeleteMapping("/{classificationCode}")
    public ResponseEntity<Object> deleteById(@PathVariable(value = "classificationCode") Integer classificationCode) {
        Optional<Classification> classificationOptional = classificationService.findById(classificationCode);
        if(classificationOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error! no classification with code: " + classificationCode);
        }
        classificationService.deleteById(classificationCode);
        return ResponseEntity.status(HttpStatus.FOUND).body("Classification " + classificationCode + " successfully deleted!");
    }
}
