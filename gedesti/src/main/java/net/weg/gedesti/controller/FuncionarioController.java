package net.weg.gedesti.controller;

import lombok.AllArgsConstructor;
import net.weg.gedesti.dto.WorkerDTO;
import net.weg.gedesti.model.entity.Worker;
import net.weg.gedesti.model.service.WorkerService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.bcrypt.BCrypt;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api/worker")
@AllArgsConstructor
public class FuncionarioController {
    private WorkerService funcionarioSerivce;

    @GetMapping
    public ResponseEntity<List<Worker>> findAll() {
        return ResponseEntity.status(HttpStatus.FOUND).body(funcionarioSerivce.findAll());
    }

    @PostMapping("/{cargoFuncionario}")
    public ResponseEntity<Object> save(@RequestBody @Valid WorkerDTO funcionarioDTO, @PathVariable(value = "cargoFuncionario") Integer cargoFuncionario) {
        funcionarioDTO.setWorkerOffice(cargoFuncionario);
        Worker funcionario = new Worker();
        BeanUtils.copyProperties(funcionarioDTO, funcionario);

        if(cargoFuncionario == 1){
            funcionario.setWorkerOffice("Solicitante");
        }else if(cargoFuncionario == 2){
            funcionario.setWorkerOffice("Analista de TI");
        }else if(cargoFuncionario == 3){
            funcionario.setWorkerOffice("Gestor de TI");
        }else if(cargoFuncionario == 4){
            funcionario.setWorkerOffice("Gerente de Negócio");
        }

//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//        funcionario.setSenhaFuncionario(encoder.encode(funcionario.getSenhaFuncionario()));

        return ResponseEntity.status(HttpStatus.CREATED).body(funcionarioSerivce.save(funcionario));
    }

    @GetMapping("/{codigoFuncionario}")
    public ResponseEntity<Object> findById(@PathVariable(value = "codigoFuncionario") Integer codigoFuncionario) {
        Optional<Worker> optionalFuncionario = funcionarioSerivce.findById(codigoFuncionario);
        if(optionalFuncionario.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro! Código de funcionario inválido");
        }
        return ResponseEntity.status(HttpStatus.FOUND).body(optionalFuncionario);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody WorkerDTO funcionarioDTO) {
        Optional<Worker> funcionario = funcionarioSerivce.findByCorporateEmail(funcionarioDTO.getCorporateEmail());
        Worker funcionarios = funcionario.get();
        if (funcionario.isPresent()) {
            if (funcionarios.getWorkerPassword().equals(funcionarioDTO.getWorkerPassword())) {
                    return ResponseEntity.status(HttpStatus.OK).body(funcionario);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERRO! Senha incorreta");
            }
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERRO! Email não existe");
        }
    }


    @DeleteMapping("/{codigoFuncionario}")
    public ResponseEntity<Object> deleteById(@PathVariable(value = "codigoFuncionario") Integer codigoFuncionario) {
        Optional<Worker> optionalFuncionario = funcionarioSerivce.findById(codigoFuncionario);
        if(optionalFuncionario.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro! Código de funcionario inválido");
        }
        funcionarioSerivce.deleteById(codigoFuncionario);
        return ResponseEntity.status(HttpStatus.OK).body("Funcionario " + codigoFuncionario + " Deletado!");
    }
}
