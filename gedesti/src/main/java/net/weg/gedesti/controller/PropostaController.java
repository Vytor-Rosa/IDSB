package net.weg.gedesti.controller;

import lombok.AllArgsConstructor;
import net.weg.gedesti.dto.PropostaDTO;
import net.weg.gedesti.model.entity.Proposta;
import net.weg.gedesti.model.service.PropostaService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
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
@RequestMapping("/api/proposta")
public class PropostaController {

    private PropostaService propostaService;

    @GetMapping
    public ResponseEntity<List<Proposta>> findAll() {
        return ResponseEntity.status(HttpStatus.FOUND).body(propostaService.findAll());
    }

    @GetMapping("/page")
    public ResponseEntity<Page<Proposta>> findAll(@PageableDefault(page = 9, size = 8, direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.FOUND).body(propostaService.findAll(pageable));
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid PropostaDTO propostaDTO) {
        Proposta proposta = new Proposta();
        BeanUtils.copyProperties(propostaDTO, proposta);
        return ResponseEntity.status(HttpStatus.CREATED).body(propostaService.save(proposta));
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Object> findById(@PathVariable(value = "codigo") Integer codigo) {
        Optional<Proposta> propostaOptional = propostaService.findById(codigo);
        if(propostaOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro! nenhuma proposta com o código: " + codigo);
        }
        return ResponseEntity.status(HttpStatus.FOUND).body(propostaOptional);
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<Object> deleteById(@PathVariable(value = "codigo") Integer codigo) {
        Optional<Proposta> propostaOptional = propostaService.findById(codigo);
        if(propostaOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro! nenhuma proposta com o código: " + codigo);
        }
        propostaService.deleteById(codigo);
        return ResponseEntity.status(HttpStatus.FOUND).body("Proposta " + codigo + " deletada com sucesso!");
    }
}
