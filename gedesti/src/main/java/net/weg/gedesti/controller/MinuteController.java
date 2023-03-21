package net.weg.gedesti.controller;

import lombok.AllArgsConstructor;
import net.weg.gedesti.model.entity.Minute;
import net.weg.gedesti.model.service.MinuteService;
import net.weg.gedesti.util.MinuteUtil;
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
@RequestMapping("/api/minutes")
public class MinuteController {

    private MinuteService minuteService;

    @GetMapping
    public ResponseEntity<List<Minute>> findAll() {
        return ResponseEntity.status(HttpStatus.FOUND).body(minuteService.findAll());
    }

    @GetMapping("/page")
    public ResponseEntity<Page<Minute>> findAll(@PageableDefault(page = 9, size = 1, direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.FOUND).body(minuteService.findAll(pageable));
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestParam(value = "minute") @Valid String minuteJson, @RequestParam(value = "minuteAttachment", required = false) MultipartFile minuteAttachment) {
        MinuteUtil util = new MinuteUtil();
        Minute minute = util.convertJsonToModel(minuteJson);
        minute.setAttachment(minuteAttachment);
        return ResponseEntity.status(HttpStatus.CREATED).body(minuteService.save(minute));
    }

    @GetMapping("/{minuteCode}")
    public ResponseEntity<Object> findById(@PathVariable(value = "minuteCode") Integer minuteCode) {
        Optional<Minute> minuteOptional = minuteService.findById(minuteCode);
        if (minuteOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error! No minute with code:" + minuteCode);
        }
        return ResponseEntity.status(HttpStatus.FOUND).body(minuteOptional);
    }


    @DeleteMapping("/{minuteCode}")
    public ResponseEntity<Object> deleteById(@PathVariable(value = "minuteCode") Integer minuteCode) {
        Optional<Minute> minuteOptional = minuteService.findById(minuteCode);
        if (minuteOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error! No minute with code: " + minuteCode);
        }
        minuteService.deleteById(minuteCode);
        return ResponseEntity.status(HttpStatus.FOUND).body("Minute " + minuteCode + " successfully deleted!");
    }
}
