package net.weg.gedesti.controller;

import lombok.AllArgsConstructor;
import net.weg.gedesti.dto.AtaDTO;
import net.weg.gedesti.dto.DespesaDTO;
import net.weg.gedesti.model.entity.Ata;
import net.weg.gedesti.model.entity.Despesa;
import net.weg.gedesti.model.service.AtaService;
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
@RequestMapping("/api/ata")
public class AtaController {

    private AtaService ataService;

    @GetMapping
    public ResponseEntity<List<Ata>> findAll(){
        return ResponseEntity.status(HttpStatus.FOUND).body(ataService.findAll());
    }

    @PostMapping
    public  ResponseEntity<Object> save(@RequestBody @Valid AtaDTO ataDTO){
        Ata ata = new Ata();
        BeanUtils.copyProperties(ataDTO, ata);
        return ResponseEntity.status(HttpStatus.FOUND).body(ataService.save(ata));
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Object> findById(@PathVariable(value = "codigo") Integer codigo) {
        Optional<Ata> ataOptional = ataService.findById(codigo);
        if (ataOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro! nenhuma despesa com o codigo: " + codigo);
        }
        return ResponseEntity.status(HttpStatus.FOUND).body(ataOptional);
    }


    @DeleteMapping("/{codigo}")
    public  ResponseEntity<Object> deleteById(@PathVariable(value = "codigo")Integer codigo){
        Optional<Ata> ataOptional = ataService.findById(codigo);
        if(ataOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro! nenhuma ata com o codigo: "+ codigo);
        }
        ataService.deleteById(codigo);
        return  ResponseEntity.status(HttpStatus.FOUND).body("Ata " +codigo+ " deletada com sucesso!");
    }
}
