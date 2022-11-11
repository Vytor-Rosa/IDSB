package net.weg.gedesti.controller;

import lombok.AllArgsConstructor;
import net.weg.gedesti.dto.MensagemDTO;
import net.weg.gedesti.model.entity.Mensagem;
import net.weg.gedesti.model.service.MensagemService;
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
@RequestMapping("/api/mensagem")
public class MensagemController {
    private MensagemService mensagemService;

    @GetMapping
    public ResponseEntity<List<Mensagem>> findAll() {
        return ResponseEntity.status(HttpStatus.FOUND).body(mensagemService.findAll());
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid MensagemDTO mensagemDTO) {
        Mensagem mensagem = new Mensagem();
        BeanUtils.copyProperties(mensagemDTO, mensagem);
        return ResponseEntity.status(HttpStatus.CREATED).body(mensagemService.save(mensagem));
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Object> findById(@PathVariable(value = "codigo") Integer codigo) {
        Optional<Mensagem> mensagemOptional = mensagemService.findById(codigo);
        if(mensagemOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro! nenhuma mensagem com o código: " + codigo);
        }
        return ResponseEntity.status(HttpStatus.FOUND).body(mensagemOptional);
    }


}
