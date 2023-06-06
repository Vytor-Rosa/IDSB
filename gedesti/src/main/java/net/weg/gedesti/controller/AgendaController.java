package net.weg.gedesti.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import net.weg.gedesti.dto.AgendaDTO;
import net.weg.gedesti.model.entity.Agenda;
import net.weg.gedesti.model.entity.Proposal;
import net.weg.gedesti.model.service.AgendaService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api/agenda")
public class AgendaController {

    private AgendaService agendaService;

    @GetMapping
    public ResponseEntity<List<Agenda>> findAll() {
        return ResponseEntity.status(HttpStatus.FOUND).body(agendaService.findAll());
    }

    @GetMapping("/page")
    public ResponseEntity<Page<Agenda>> findAll(@PageableDefault(page = 0, value = 1, size = 5, direction = Sort.Direction.ASC) Pageable pageable) {
        int pageNumber = pageable.getPageNumber();
        pageable = PageRequest.of(pageNumber > 0 ? pageNumber - 1 : 0, pageable.getPageSize(), pageable.getSort());
        return ResponseEntity.status(HttpStatus.FOUND).body(agendaService.findAll(pageable));
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid AgendaDTO agendaDTO) {
        Agenda agenda = new Agenda();
        BeanUtils.copyProperties(agendaDTO, agenda);

        return ResponseEntity.status(HttpStatus.CREATED).body(agendaService.save(agenda));
    }

    @GetMapping("/{agendaCode}")
    public ResponseEntity<Object> findById(@PathVariable(value = "agendaCode") Integer agendaCode) {
        Optional<Agenda> optionalAgenda = agendaService.findById(agendaCode);

        if (optionalAgenda.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error! No agenda with code: " + agendaCode);
        }

        List<Agenda> listAgenda = new ArrayList<>();
        listAgenda.add(optionalAgenda.get());
        return ResponseEntity.status(HttpStatus.FOUND).body(listAgenda);
    }

    @DeleteMapping("/{agendaCode}")
    public ResponseEntity<Object> deleteById(@PathVariable(value = "agendaCode") Integer agendaCode) {
        Optional<Agenda> agendaOptional = agendaService.findById(agendaCode);
        if (agendaOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error! No agenda with code: " + agendaCode);
        }
        agendaService.deleteById(agendaCode);
        return ResponseEntity.status(HttpStatus.FOUND).body("Agenda " + agendaCode + " successfully deleted!");
    }

    @GetMapping("/proposal/{proposalCode}")
    public ResponseEntity<Object> findByProposalCode(@PathVariable(value = "proposalCode") Proposal proposalCode) {
        List<Agenda> listAgenda = agendaService.findAll();
        for (Agenda agenda : listAgenda) {
            if (agenda.getProposals().contains(proposalCode)) {
                return ResponseEntity.status(HttpStatus.FOUND).body(agenda);
            }
        }
        return ResponseEntity.status(HttpStatus.FOUND).body("Error! No agenda with proposal code: " + proposalCode);
    }

}
