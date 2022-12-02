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
    private MessageService messageService;

    @GetMapping
    public ResponseEntity<List<Message>> findAll() {
        return ResponseEntity.status(HttpStatus.FOUND).body(messageService.findAll());
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid MessageDTO messageDTO) {
        Message message = new Message();
        BeanUtils.copyProperties(messageDTO, message);
        return ResponseEntity.status(HttpStatus.CREATED).body(messageService.save(message));
    }

    @GetMapping("/{messageCode}")
    public ResponseEntity<Object> findById(@PathVariable(value = "messageCode") Integer messageCode) {
        Optional<Message> messageOptional = messageService.findById(messageCode);
        if(messageOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error! No message with code: " + messageCode);
        }
        return ResponseEntity.status(HttpStatus.FOUND).body(messageOptional);
    }


}
