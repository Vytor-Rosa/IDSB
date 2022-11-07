package net.weg.gedesti.controller;

import lombok.AllArgsConstructor;
import net.weg.gedesti.dto.PautaDTO;
import net.weg.gedesti.model.entity.Comissao;
import net.weg.gedesti.model.entity.Pauta;
import net.weg.gedesti.model.service.ComissaoService;
import net.weg.gedesti.model.service.PautaService;
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
@RequestMapping("/api/pauta")
public class PautaController {
    private PautaService pautaService;
    private ComissaoService comissaoService;

    @GetMapping
    public ResponseEntity<List<Pauta>> findAll() {
        return ResponseEntity.status(HttpStatus.FOUND).body(pautaService.findAll());
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid PautaDTO pautaDTO) {
        Pauta pauta = new Pauta();
        BeanUtils.copyProperties(pautaDTO, pauta);
        Pauta pautaSalva = pautaService.save(pauta);
        Integer codigoPauta;
        for(Comissao comissao : pautaSalva.getFuncionarios()){
            comissao.setCodigoPauta(pautaSalva);
//            comissaoService.save(comissao);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body("Pauta salva." + pautaSalva);
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Object> findById(@PathVariable(value = "codigo") Integer codigo) {
        Optional<Pauta> pautaOptional = pautaService.findById(codigo);
        if(pautaOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro nenhuma pauta com o codigo: " + codigo);
        }
        return ResponseEntity.status(HttpStatus.FOUND).body(pautaOptional);
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<Object> deleteById(@PathVariable(value = "codigo") Integer codigo) {
        Optional<Pauta> pautaOptional = pautaService.findById(codigo);
        if(pautaOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro nenhuma pauta com o codigo: " + codigo);
        }
        pautaService.deleteById(codigo);
        return ResponseEntity.status(HttpStatus.FOUND).body("Pauta " + codigo + " deletada com sucesso!");
    }
}
