package net.weg.gedesti.controller;

import lombok.AllArgsConstructor;
import net.weg.gedesti.component.UserPresenceManager;
import net.weg.gedesti.dto.WorkerDTO;
import net.weg.gedesti.model.entity.Worker;
import net.weg.gedesti.model.service.WorkerService;
import net.weg.gedesti.repository.WorkerRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.net.weg.gedesti.security.crypto.bcrypt.BCrypt;
//import org.springframework.net.weg.gedesti.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/worker")
@AllArgsConstructor
public class WorkerController {
    private WorkerService workerSerivce;
    private WorkerRepository workerRepository;

    private UserPresenceManager userPresenceManager;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @GetMapping
    public ResponseEntity<List<Worker>> findAll() {
        return ResponseEntity.status(HttpStatus.FOUND).body(workerSerivce.findAll());
    }

    @PostMapping("/{workerOffice}")
    public ResponseEntity<Object> save(@RequestBody @Valid WorkerDTO workerDTO, @PathVariable(value = "workerOffice") Integer workerOffice) {
//        workerDTO.setWorkerOffice(workerOffice);
        Worker worker = new Worker();
        BeanUtils.copyProperties(workerDTO, worker);

        if (workerOffice == 1) {
            worker.setWorkerOffice("requester");
        } else if (workerOffice == 2) {
            worker.setWorkerOffice("analyst");
        } else if (workerOffice == 3) {
            worker.setWorkerOffice("ti");
        } else if (workerOffice == 4) {
            worker.setWorkerOffice("business");
        }

//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//        worker.setWorkerPassword(encoder.encode(worker.getWorkerPassword()));

        return ResponseEntity.status(HttpStatus.CREATED).body(workerSerivce.save(worker));
    }

    @GetMapping("/{workerCode}")
    public ResponseEntity<Object> findById(@PathVariable(value = "workerCode") Integer workerCode) {
        Optional<Worker> optionalWorker = workerSerivce.findById(workerCode);
        if (optionalWorker.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error! No worker with code: " + workerCode);
        }
        return ResponseEntity.status(HttpStatus.FOUND).body(optionalWorker);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody @Valid WorkerDTO workerDTO) {
        Optional<Worker> worker = workerSerivce.findByCorporateEmail(workerDTO.getCorporateEmail());
        Worker workers = worker.get();
        if (worker.isPresent()) {
            if (workers.getWorkerPassword().equals(workerDTO.getWorkerPassword())) {
                return ResponseEntity.status(HttpStatus.OK).body(worker);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error! Incorrect password!");
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error! Email doesn't exist!");
    }


    @DeleteMapping("/{workerCode}")
    public ResponseEntity<Object> deleteById(@PathVariable(value = "workerCode") Integer workerCode) {
        Optional<Worker> optionalWorker = workerSerivce.findById(workerCode);
        if (optionalWorker.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error! No worker with code:" + workerCode);
        }
        workerSerivce.deleteById(workerCode);
        return ResponseEntity.status(HttpStatus.OK).body("Worker " + workerCode + " successfully deleted!");
    }

    @Modifying
    @Transactional
    @PutMapping("/voiceCommand/{workerCode}")
    public ResponseEntity<Object> updateVoiceCommand(@PathVariable(value = "workerCode") Integer workerCode, @RequestBody WorkerDTO workerDTO) {
        if (!workerSerivce.existsById(workerCode)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("doesn't exist");
        }

        Worker worker = workerRepository.findById(workerCode).get();
        worker.setVoiceCommand(workerDTO.getVoiceCommand());
        return ResponseEntity.status(HttpStatus.CREATED).body(workerRepository.saveAndFlush(worker));
    }

    @Modifying
    @Transactional
    @PutMapping("/language/{workerCode}")
    public ResponseEntity<Object> updateClassification(@PathVariable(value = "workerCode") Integer workerCode, @RequestBody WorkerDTO workerDTO) {
        if (!workerSerivce.existsById(workerCode)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("doesn't exist");
        }

        Worker worker = workerRepository.findById(workerCode).get();
        worker.setLanguage(workerDTO.getLanguage());
        return ResponseEntity.status(HttpStatus.CREATED).body(workerRepository.saveAndFlush(worker));
    }

    @Modifying
    @Transactional
    @PutMapping("/pounds/{workerCode}")
    public ResponseEntity<Object> updatePounds(@PathVariable(value = "workerCode") Integer workerCode, @RequestBody WorkerDTO workerDTO) {
        if (!workerSerivce.existsById(workerCode)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("doesn't exist");
        }

        Worker worker = workerRepository.findById(workerCode).get();
        worker.setPounds(workerDTO.getPounds());
        return ResponseEntity.status(HttpStatus.CREATED).body(workerRepository.saveAndFlush(worker));
    }


    @GetMapping("/user/{userId}/online")
    public ResponseEntity<Boolean> isUserOnline(@PathVariable("userId") Integer userId) {
        boolean isOnline = userPresenceManager.isUserOnline(workerSerivce.findById(userId).get().getCorporateEmail());
        return ResponseEntity.ok(isOnline);
    }
}
