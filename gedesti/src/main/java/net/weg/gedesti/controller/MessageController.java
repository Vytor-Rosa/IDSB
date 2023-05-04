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
    public ResponseEntity<?> demandMessages(@PathVariable(value = "demandCode") Integer demandCode) {
        System.out.println("Entrou no get");
        List<Demand> demandList = demandService.findByDemandCode(demandCode);
        for(Demand demand : demandList){
            if(demand.getActiveVersion() == true){
                return ResponseEntity.ok(messageService.findAllByDemand(demand.getDemandCode()));
            }
        }
        return ResponseEntity.status(HttpStatus.FOUND).body(demandCode);
    }

    @MessageMapping("/demand/{id}") // Mandando a requisição
    @SendTo("/{id}/chat") // Buscando a requisição toda hora pra ver se tem mensagem nova
    public Message save(@Payload MessageDTO messageDTO){
        System.out.println("Entrou no save");
        Message message = new Message();
        BeanUtils.copyProperties(messageDTO, message);
        return  messageService.save(message);
    }
    @GetMapping("/worker/{workerCode}")
    public ResponseEntity<Object> findAllByDemandRequester(@PathVariable(value = "workerCode") Worker workerCode){
        List<Demand> demandList = demandService.findAllByRequesterRegistration(workerCode);
        for (int i = 0; i <= demandList.size(); i++){
            List<Message> messages = messageService.findAllByDemand(demandList.get(i).getDemandCode());
            if(!messages.isEmpty()){
                return ResponseEntity.status(HttpStatus.OK).body(true);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(false);
    }

//    @GetMapping("/worker/{workerCode}/demand")
//    public ResponseEntity<Object> findChatByDemand(@PathVariable(value = "workerCode") Integer workerCode) {
//        List<Demand> allDemands = demandService.findAll();
//        List<Demand> demandList = new ArrayList<>();
//        List<Worker> allWorkers = workerService.findAll();
//        for (Demand demand : allDemands) {
//            for(Worker worker : allWorkers) {
//                if (demand.getRequesterRegistration().equals(workerCode) || demand.getWorkerRegistration().equals(workerCode)) {
//                    demandList.add(demand);
//                }
//            }
//            List<Message> messages = messageService.findAllBySenderAndDemand(workerCode, demand);
//            if (demand.getRequesterRegistration().equals(workerCode)) {
//                if (messages.isEmpty()) {
//                    demandList.add(demand);
//                }
//            }
//        }
//        return ResponseEntity.status(HttpStatus.OK).body(demandList);
//    }

}
