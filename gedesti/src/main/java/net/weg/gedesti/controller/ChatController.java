package net.weg.gedesti.controller;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import lombok.AllArgsConstructor;
import net.weg.gedesti.dto.ChatDTO;
import net.weg.gedesti.model.entity.Chat;
import net.weg.gedesti.model.service.ChatService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {

    private ChatService chatService;

    @GetMapping
    public ResponseEntity<List<Chat>> findAll() {
        return ResponseEntity.status(HttpStatus.FOUND).body(chatService.findAll());
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid ChatDTO chatDTO) {
        Chat chat = new Chat();
        BeanUtils.copyProperties(chatDTO, chat);
        return ResponseEntity.status(HttpStatus.CREATED).body(chatService.save(chat));
    }

    @GetMapping("/{chatCode}")
    public ResponseEntity<Object> findById(@PathVariable(value = "chatCode") Integer chatCode) {
        Optional<Chat> chatOptional = chatService.findById(chatCode);
        if (chatOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error! Invalid chat code: " + chatCode);
        }
        return ResponseEntity.status(HttpStatus.FOUND).body(chatOptional);
    }
}
