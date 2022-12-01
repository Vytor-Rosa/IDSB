package net.weg.gedesti.controller;

import lombok.AllArgsConstructor;
import net.weg.gedesti.model.entity.Ata;
import net.weg.gedesti.model.service.AtaService;
import net.weg.gedesti.util.AtaUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
@RequestMapping("/api/minutes")
public class AtaController {

    private AtaService ataService;

    @GetMapping
    public ResponseEntity<List<Ata>> findAll(){
        return ResponseEntity.status(HttpStatus.FOUND).body(ataService.findAll());
    }

    @GetMapping("/page")
    public ResponseEntity<Page<Ata>> findAll(@PageableDefault(page = 9, size = 1, direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.FOUND).body(ataService.findAll(pageable));
    }

    @PostMapping
    public  ResponseEntity<Object> save(@RequestParam(value = "ata") @Valid String ataJson, @RequestParam(value = "anexo") MultipartFile anexo){
        AtaUtil util = new AtaUtil();
        Ata ata = util.convertJsonToModel(ataJson);
        ata.setAttachment(anexo);
        return ResponseEntity.status(HttpStatus.CREATED).body(ataService.save(ata));
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
