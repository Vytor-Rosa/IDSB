package net.weg.gedesti.controller;

import lombok.AllArgsConstructor;
import net.weg.gedesti.model.entity.Attachment;
import net.weg.gedesti.model.service.AttachmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@AllArgsConstructor
@RequestMapping("/api/attachment")
public class AttachmentController {
    private AttachmentService attachmentService;

    @PostMapping
    public ResponseEntity<Object> save(@RequestParam("file") MultipartFile file) throws IOException {
        Attachment attachment = new Attachment();
        attachment.setName(file.getOriginalFilename());
        attachment.setType(file.getContentType());
        attachment.setDice(file.getBytes());
        return ResponseEntity.ok(attachmentService.save(attachment));
    }
}
