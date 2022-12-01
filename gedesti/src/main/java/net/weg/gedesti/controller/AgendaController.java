package net.weg.gedesti.controller;

import lombok.AllArgsConstructor;
import net.weg.gedesti.dto.AgendaDTO;
import net.weg.gedesti.model.entity.Agenda;
import net.weg.gedesti.model.service.AgendaService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
@RequestMapping("/api/agenda")
public class AgendaController {
    private AgendaService pautaService;

    @GetMapping
    public ResponseEntity<List<Agenda>> findAll() {
        return ResponseEntity.status(HttpStatus.FOUND).body(pautaService.findAll());
    }

    @GetMapping("/page")
    public ResponseEntity<Page<Agenda>> findAll(@PageableDefault(page = 9, size = 8, direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.FOUND).body(pautaService.findAll(pageable));
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid AgendaDTO agendaDTO) {
        Agenda agenda = new Agenda();
        BeanUtils.copyProperties(agendaDTO, agenda);

        return ResponseEntity.status(HttpStatus.CREATED).body(pautaService.save(agenda));
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Object> findById(@PathVariable(value = "codigo") Integer codigo) {
        Optional<Agenda> optionalAgenda = pautaService.findById(codigo);

        if (optionalAgenda.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro nenhuma pauta com o codigo: " + codigo);
        }

        List<Agenda> listAgenda = new ArrayList<>();
        listAgenda.add(optionalAgenda.get());
        return ResponseEntity.status(HttpStatus.FOUND).body(listAgenda);
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<Object> deleteById(@PathVariable(value = "codigo") Integer codigo) {
        Optional<Agenda> pautaOptional = pautaService.findById(codigo);
        if (pautaOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro nenhuma pauta com o codigo: " + codigo);
        }
        pautaService.deleteById(codigo);
        return ResponseEntity.status(HttpStatus.FOUND).body("Pauta " + codigo + " deletada com sucesso!");
    }
}
