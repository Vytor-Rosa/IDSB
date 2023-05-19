package net.weg.gedesti.controller;

import lombok.AllArgsConstructor;
import net.weg.gedesti.dto.BuDTO;
import net.weg.gedesti.dto.ClassificationDTO;
import net.weg.gedesti.dto.NotificationDTO;
import net.weg.gedesti.model.entity.Bu;
import net.weg.gedesti.model.entity.Classification;
import net.weg.gedesti.model.entity.Minute;
import net.weg.gedesti.model.entity.Notification;
import net.weg.gedesti.model.service.NotificationService;
import net.weg.gedesti.repository.ClassificationRepository;
import net.weg.gedesti.repository.NotificationRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api/notification")
public class NotificationController {

    private NotificationService notificationService;
    private NotificationRepository notificationRepository;


    @GetMapping
    public ResponseEntity<List<Notification>> findAll() {
        return ResponseEntity.status(HttpStatus.FOUND).body(notificationService.findAll());

    }

    @MessageMapping("/worker/{workerCode}")
    @SendTo("/notifications/{workerCode}")
    public ResponseEntity<?> save(@Payload NotificationDTO notificationDTO) {
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

    @Modifying
    @Transactional
    @PutMapping("/update/{notificationCode}")
    public ResponseEntity<Object> updateVisualized(@PathVariable(value = "notificationCode") Integer notificationCode) {

        if (!notificationService.existsById(notificationCode)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error! no classification with code: " + notificationCode);
        }


        Notification notification = notificationRepository.findById(notificationCode).get();
        notification.setVisualized(true);

        notificationRepository.saveAndFlush(notification);

        return ResponseEntity.status(HttpStatus.OK).body("Classification " + notificationCode + " successfully updated!");
    }
}
