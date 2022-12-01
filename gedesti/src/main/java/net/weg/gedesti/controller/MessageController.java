package net.weg.gedesti.controller;

import lombok.AllArgsConstructor;
import net.weg.gedesti.dto.MessageDTO;
import net.weg.gedesti.model.entity.Message;
import net.weg.gedesti.model.service.MessageService;
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
@RequestMapping("/api/message")
public class MessageController {
    private MessageService mensagemService;

    @GetMapping
    public ResponseEntity<List<Message>> findAll() {
        return ResponseEntity.status(HttpStatus.FOUND).body(mensagemService.findAll());
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid MessageDTO mensagemDTO) {
        Message mensagem = new Message();
        BeanUtils.copyProperties(mensagemDTO, mensagem);
        return ResponseEntity.status(HttpStatus.CREATED).body(mensagemService.save(mensagem));
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Object> findById(@PathVariable(value = "codigo") Integer codigo) {
        Optional<Message> mensagemOptional = mensagemService.findById(codigo);
        if(mensagemOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro! nenhuma mensagem com o código: " + codigo);
        }
        return ResponseEntity.status(HttpStatus.FOUND).body(mensagemOptional);
    }


}
