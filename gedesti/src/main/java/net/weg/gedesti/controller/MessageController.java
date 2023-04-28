package net.weg.gedesti.controller;

import lombok.AllArgsConstructor;
import net.weg.gedesti.dto.MessageDTO;
import net.weg.gedesti.model.entity.Demand;
import net.weg.gedesti.model.entity.Message;
import net.weg.gedesti.model.entity.Worker;
import net.weg.gedesti.model.service.DemandService;
import net.weg.gedesti.model.service.MessageService;
import net.weg.gedesti.model.service.WorkerService;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api/message")
public class MessageController {
    private MessageService messageService;
    private DemandService demandService;
    private WorkerService workerService;

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
    public ResponseEntity<?> mensagensLivro(@PathVariable(value = "demandCode") Integer demandCode) {
        return ResponseEntity.ok(messageService.findAllByDemand(demandService.findById(demandCode).get()));
    }


    @MessageMapping("/demand/{id}") // Mandando a requisição
    @SendTo("/{id}/chat") // Buscando a requisição toda hora pra ver se tem mensagem nova
    public Message save(@Payload MessageDTO messageDTO){

        Message message = new Message();
        BeanUtils.copyProperties(messageDTO, message);
        return  messageService.save(message);
    }
    @GetMapping("/worker/{workerCode}")
    public ResponseEntity<Object> findAllByDemandRequester(@PathVariable(value = "workerCode") Worker workerCode){
        List<Demand> demandList = demandService.findAllByRequesterRegistration(workerCode);
        for (int i = 0; i <= demandList.size(); i++){
            List<Message> messages = messageService.findAllByDemand(demandList.get(i));
            if(!messages.isEmpty()){
                return ResponseEntity.status(HttpStatus.OK).body(true);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(false);
    }

}
