package net.weg.gedesti.controller;

import lombok.AllArgsConstructor;
import net.weg.gedesti.dto.DemandDTO;
import net.weg.gedesti.model.entity.Classification;
import net.weg.gedesti.model.entity.Demand;
import net.weg.gedesti.model.entity.Worker;
import net.weg.gedesti.model.service.ClassificationService;
import net.weg.gedesti.model.service.DemandService;
import net.weg.gedesti.model.service.WorkerService;
import net.weg.gedesti.repository.DemandRepository;
import net.weg.gedesti.util.DemandUtil;
import org.springframework.beans.BeanUtils;
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
    private DemandService demandService;
    private WorkerService workerService;
    private ClassificationService classificationService;
    private DemandRepository demandRepository;

    @GetMapping
    public ResponseEntity<List<Demand>> findAll() {
        return ResponseEntity.status(HttpStatus.FOUND).body(demandService.findAll());
    }

    @GetMapping("/page")
    public ResponseEntity<Page<Demand>> findAll(@PageableDefault(page = 9, size = 8, direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.FOUND).body(demandService.findAll(pageable));
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestParam(value = "demand") @Valid String demandJson, @RequestParam(value = "demandAttachment", required = false) MultipartFile demandAttachment) {
        DemandUtil demandUtil = new DemandUtil();
        Demand demand = demandUtil.convertJsonToModel(demandJson);
        if(demandAttachment != null) {
            demand.setDemandAttachment(demandAttachment);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(demandService.save(demand));
    }

    @GetMapping("/{demandCode}")
    public ResponseEntity<Object> findById(@PathVariable(value = "demandCode") Integer demandCode) {
        Optional<Demand> demandOptional = demandService.findById(demandCode);
        if (demandOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error! No demand with code: " + demandCode);
        }

        return ResponseEntity.status(HttpStatus.FOUND).body(demandOptional);
    }

    @DeleteMapping("/{demandCode}")
    public ResponseEntity<Object> deleteById(@PathVariable(value = "demandCode") Integer demandCode) {
        Optional<Demand> demandOptional = demandService.findById(demandCode);
        if (demandOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error! No demand with code: " + demandCode);
        }
        demandService.deleteById(demandCode);
        return ResponseEntity.status(HttpStatus.FOUND).body("Demand " + demandCode + " successfully deleted!");
    }

    @Modifying
    @Transactional
    @PutMapping("/{demandCode}")
    public ResponseEntity<Object> update(@RequestParam(value = "demand") @Valid String demandJson,
                                         @RequestParam(value = "demandAttachment") MultipartFile demandAttachment,
                                         @PathVariable(value = "demandCode") Integer demandCode) {
        if (!demandService.existsById(demandCode)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("doesn't exist");
        }

        DemandUtil demandUtil = new DemandUtil();
        Demand demand = demandUtil.convertJsonToModel(demandJson);
        demand.setDemandAttachment(demandAttachment);
        demand.setDemandCode(demandCode);

        return ResponseEntity.status(HttpStatus.CREATED).body(demandService.save(demand));
    }

    @Modifying
    @Transactional
    @PutMapping("/updateclassification/{demandCode}")
    public ResponseEntity<Object> updateClassification(@PathVariable(value = "demandCode") Integer demandCode, @RequestBody DemandDTO demandDTO) {
        if (!demandService.existsById(demandCode)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("doesn't exist");
        }

        Demand demand = demandRepository.findById(demandCode).get();
        demand.setClassification(demandDTO.getClassification());


        return ResponseEntity.status(HttpStatus.CREATED).body(demandRepository.saveAndFlush(demand));
    }

    @Modifying
    @Transactional
    @PutMapping("/updatestatus/{demandCode}")
    public ResponseEntity<Object> updateStatus(@PathVariable(value = "demandCode") Integer demandCode, @RequestBody DemandDTO demandDTO) {
        if (!demandService.existsById(demandCode)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("doesn't exist");
        }

        Demand demand = demandRepository.findById(demandCode).get();
        demand.setDemandStatus(demandDTO.getDemandStatus());


        return ResponseEntity.status(HttpStatus.CREATED).body(demandRepository.saveAndFlush(demand));
    }
}
