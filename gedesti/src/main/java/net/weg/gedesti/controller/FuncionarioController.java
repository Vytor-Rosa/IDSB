package net.weg.gedesti.controller;

import lombok.AllArgsConstructor;
import net.weg.gedesti.dto.FuncionarioDTO;
import net.weg.gedesti.model.entity.Funcionario;
import net.weg.gedesti.model.service.FuncionarioService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api/funcionarios")
@AllArgsConstructor
public class FuncionarioController {
    private FuncionarioService funcionarioSerivce;

    @GetMapping
    public ResponseEntity<List<Funcionario>> findAll() {
        return ResponseEntity.status(HttpStatus.FOUND).body(funcionarioSerivce.findAll());
    }

    @PostMapping("/{cargoFuncionario}")
    public ResponseEntity<Object> save(@RequestBody @Valid FuncionarioDTO funcionarioDTO, @PathVariable(value = "cargoFuncionario") Integer cargoFuncionario) {
        funcionarioDTO.setCargoFuncionario(cargoFuncionario);
        Funcionario funcionario = new Funcionario();
        BeanUtils.copyProperties(funcionarioDTO, funcionario);
        return ResponseEntity.status(HttpStatus.CREATED).body(funcionarioSerivce.save(funcionario));
    }

    @GetMapping("/{codigoFuncionario}")
    public ResponseEntity<Object> findById(@PathVariable(value = "codigoFuncionario") Integer codigoFuncionario) {
        Optional<Funcionario> optionalFuncionario = funcionarioSerivce.findById(codigoFuncionario);
        if(optionalFuncionario.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro! Código de funcionario inválido");
        }
        return ResponseEntity.status(HttpStatus.FOUND).body(optionalFuncionario);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody FuncionarioDTO funcionarioDTO) {
        Optional<Funcionario> funcionario = funcionarioSerivce.findByEmailCorporativo(funcionarioDTO.getEmailCorporativo());
        Funcionario funcionarios = funcionario.get();
        if (funcionario.isPresent()) {
            if (funcionarios.getSenhaFuncionario().equals(funcionarioDTO.getSenhaFuncionario())) {
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
        Optional<Funcionario> optionalFuncionario = funcionarioSerivce.findById(codigoFuncionario);
        if(optionalFuncionario.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro! Código de funcionario inválido");
        }
        funcionarioSerivce.deleteById(codigoFuncionario);
        return ResponseEntity.status(HttpStatus.OK).body("Funcionario " + codigoFuncionario + " Deletado!");
    }
}
