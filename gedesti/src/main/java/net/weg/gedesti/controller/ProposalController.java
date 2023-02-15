package net.weg.gedesti.controller;

import lombok.AllArgsConstructor;
import net.weg.gedesti.dto.ProposalDTO;
import net.weg.gedesti.model.entity.Proposal;
import net.weg.gedesti.model.service.ProposalService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
@RequestMapping("/api/proposal")
public class ProposalController {

    private ProposalService proposalService;

    @GetMapping
    public ResponseEntity<List<Proposal>> findAll() {
        return ResponseEntity.status(HttpStatus.FOUND).body(proposalService.findAll());
    }

    @GetMapping("/page")
    public ResponseEntity<Page<Proposal>> findAll(@PageableDefault(page = 9, size = 8, direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.FOUND).body(proposalService.findAll(pageable));
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid ProposalDTO proposalDTO) {
        Proposal proposal = new Proposal();
        BeanUtils.copyProperties(proposalDTO, proposal);
        return ResponseEntity.status(HttpStatus.CREATED).body(proposalService.save(proposal));
    }

    @GetMapping("/{proposalCode}")
    public ResponseEntity<Object> findById(@PathVariable(value = "proposalCode") Integer proposalCode) {
        Optional<Proposal> proposalOptional = proposalService.findById(proposalCode);
        if (proposalOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error! No proposal with code: " + proposalCode);
        }
        return ResponseEntity.status(HttpStatus.FOUND).body(proposalOptional);
    }

    @DeleteMapping("/{proposalCode}")
    public ResponseEntity<Object> deleteById(@PathVariable(value = "proposalCode") Integer proposalCode) {
        Optional<Proposal> proposalOptional = proposalService.findById(proposalCode);
        if (proposalOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error! No proposal with code: " + proposalCode);
        }
        proposalService.deleteById(proposalCode);
        return ResponseEntity.status(HttpStatus.FOUND).body("Proposal " + proposalCode + " successfully deleted!");
    }

    @GetMapping("/demand/{demandCode}")
    public ResponseEntity<Object> findByDemand(@PathVariable(value = "demandCode") Integer demandCode) {
        Optional<Proposal> proposalOptional = proposalService.findByDemand(demandCode);
        if(proposalOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Demand " + demandCode + " doesn't exists");
        }else{
            return ResponseEntity.status(HttpStatus.FOUND).body(proposalService.findByDemand(demandCode));
        }
    }
}
