package net.weg.gedesti.controller;

import lombok.AllArgsConstructor;
import net.weg.gedesti.dto.MessageDTO;
import net.weg.gedesti.model.entity.Demand;
import net.weg.gedesti.model.entity.Message;
import net.weg.gedesti.model.entity.Worker;
import net.weg.gedesti.model.service.DemandService;
import net.weg.gedesti.model.service.MessageService;
import net.weg.gedesti.model.service.WorkerService;
import net.weg.gedesti.repository.MessageRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/message")
public class MessageController {
    private MessageService messageService;
    private DemandService demandService;
    private WorkerService workerService;
    private MessageRepository messageRepository;

    @GetMapping
    public ResponseEntity<List<Message>> findAll() {
        return ResponseEntity.status(HttpStatus.FOUND).body(messageService.findAll());
    }

//    @PostMapping
//    public ResponseEntity<Object> save(@RequestBody @Valid MessageDTO messageDTO) {
//        Message message = new Message();
//        BeanUtils.copyProperties(messageDTO, message);
//        return ResponseEntity.status(HttpStatus.CREATED).body(messageService.save(message));
//    }

//    @GetMapping("/{messageCode}")
//    public ResponseEntity<Object> findById(@PathVariable(value = "messageCode") Integer messageCode) {
//        Optional<Message> messageOptional = messageService.findById(messageCode);
//        if (messageOptional.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error! No message with code: " + messageCode);
//        }
//        return ResponseEntity.status(HttpStatus.FOUND).body(messageOptional);
//    }
//

    @GetMapping("/{demandCode}")
    public ResponseEntity<?> demandMessages(@PathVariable(value = "demandCode") Integer demandCode) {
        List<Demand> demandList = demandService.findByDemandCode(demandCode);
        for (Demand demand : demandList) {
            if (demand.getActiveVersion() == true) {
                return ResponseEntity.ok(messageService.findAllByDemand(demand.getDemandCode()));
            }
        }
        return ResponseEntity.status(HttpStatus.FOUND).body(demandCode);
    }

    @MessageMapping("/demand/{id}")
    @SendTo("/{id}/chat")
    public Message save(@Payload MessageDTO messageDTO) {
        Message message = new Message();
        BeanUtils.copyProperties(messageDTO, message);
        if (message.getAttachment() == null && message.getMessage() == null) {
            throw new RuntimeException("Error! Message is empty!");
        }
        return messageService.save(message);
    }

    @GetMapping("/worker/{workerCode}")
    public ResponseEntity<Object> findAllByDemandRequester(@PathVariable(value = "workerCode") Worker workerCode) {
        List<Demand> demandList = demandService.findAllByRequesterRegistration(workerCode);
        for (int i = 0; i < demandList.size(); i++) {
            List<Message> messages = messageService.findAllByDemand(demandList.get(i).getDemandCode());
            if (!messages.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body(true);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(false);
    }

    @GetMapping("/worker/demand/{workerCode}")
    public ResponseEntity<Object> findChatByDemand(@PathVariable(value = "workerCode") Worker workerCode) {
        List<Demand> demandList = demandService.findAll();
        List<Message> messageList = new ArrayList<>();
        Demand demandFinal = new Demand();

        for (Demand demand : demandList) {
            List<Demand> demandsFinal = demandService.findAllByDemandCode(demand.getDemandCode());
            for (Demand demandActive : demandsFinal) {
                if (demandActive.getActiveVersion() == true) {
                    demandFinal = demandActive;
                }
            }

            if (!messageService.findAllBySenderAndDemand(workerCode, demandFinal.getDemandCode()).isEmpty()) {
                if (!messageList.contains(messageService.findTopByDemandCodeOrderByMessageCodeDesc(demandFinal.getDemandCode()))) {
                    messageList.add(messageService.findTopByDemandCodeOrderByMessageCodeDesc(demandFinal.getDemandCode()));
                }
            }

            if (demandFinal.getRequesterRegistration().getWorkerCode() == workerCode.getWorkerCode()) {
                if (!messageList.contains(messageService.findTopByDemandCodeOrderByMessageCodeDesc(demandFinal.getDemandCode())) && messageService.findTopByDemandCodeOrderByMessageCodeDesc(demandFinal.getDemandCode()) != null) {
                    messageList.add(messageService.findTopByDemandCodeOrderByMessageCodeDesc(demandFinal.getDemandCode()));
                }
            }
        }
        System.out.println(messageList);
        return ResponseEntity.status(HttpStatus.FOUND).body(messageList);
    }

    @GetMapping("/worker/demand/{workerCode}/{demandCode}")
    public ResponseEntity<?> findSender(@PathVariable(value = "workerCode") Worker workerCode, @PathVariable(value = "demandCode") Integer demandCode) {
        List<Demand> demandList = demandService.findByDemandCode(demandCode);
        Demand demandFinal = new Demand();
        for (Demand demand : demandList) {
            if (demand.getActiveVersion() == true) {
                demandFinal = demand;

            }
        }
        List<Message> messageList = messageService.findAllByDemand(demandFinal.getDemandCode());
        for (Message message : messageList) {
            if (message.getSender().getWorkerCode() != workerCode.getWorkerCode()) {
                return ResponseEntity.status(HttpStatus.FOUND).body(message.getSender());
            }
        }
        return ResponseEntity.status(HttpStatus.FOUND).body("OUT");
    }

    @Modifying
    @Transactional
    @PutMapping("/viewed/{messageCode}")
    public ResponseEntity<Object> update(@PathVariable(value = "messageCode") Integer messageCode) {
        Message message = messageService.findById(messageCode).get();
        message.setViewed(true);
        return ResponseEntity.status(HttpStatus.OK).body(messageRepository.saveAndFlush(message));
    }

    @GetMapping("/notviewed/{workerCode}/{demandCode}")
    public ResponseEntity<Object> notViewed(@PathVariable(value = "workerCode") Integer workerCode, @PathVariable(value = "demandCode") Integer demandCode) {
        List<Message> messageList = new ArrayList<>();
        List<Message> messagesDemand = messageService.findAllByDemand(demandCode);
        for (int i = 0; i < messagesDemand.size(); i++) {
            if (messagesDemand.get(i).getSender().getWorkerCode() != workerCode) {
                if (messagesDemand.get(i).getViewed() == null) {
                    messageList.add(messagesDemand.get(i));
                }
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(messageList);
    }

}