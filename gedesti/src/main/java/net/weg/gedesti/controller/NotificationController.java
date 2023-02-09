package net.weg.gedesti.controller;

import lombok.AllArgsConstructor;
import net.weg.gedesti.dto.BuDTO;
import net.weg.gedesti.dto.NotificationDTO;
import net.weg.gedesti.model.entity.Bu;
import net.weg.gedesti.model.entity.Minute;
import net.weg.gedesti.model.entity.Notification;
import net.weg.gedesti.model.service.NotificationService;
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
@RequestMapping("/api/notification")
public class NotificationController {

    private NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<Notification>> findAll() {
        return ResponseEntity.status(HttpStatus.FOUND).body(notificationService.findAll());

    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid NotificationDTO notificationDTO) {

        Notification notification = new Notification();
        BeanUtils.copyProperties(notificationDTO, notification);
        return ResponseEntity.status(HttpStatus.CREATED).body(notificationService.save(notification));
    }

    @GetMapping("/{notificationCode}")
    public ResponseEntity<Object> existsById(@PathVariable(value = "notificationCode") Integer notificationCode) {
        Optional<Notification> notificationOptional = notificationService.findById(notificationCode);
        if (notificationOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error! No minute with code:" + notificationCode);
        }
        return ResponseEntity.status(HttpStatus.FOUND).body(notificationOptional);
    }

    @DeleteMapping("/{notificationCode}")
    public void deleteById(Integer integer) {
        notificationService.deleteById(integer);
    }
}
