package net.weg.gedesti.controller;

import lombok.AllArgsConstructor;
import net.weg.gedesti.dto.PautaDTO;
import net.weg.gedesti.model.entity.Comissao;
import net.weg.gedesti.model.entity.Demanda;
import net.weg.gedesti.model.entity.Funcionario;
import net.weg.gedesti.model.entity.Pauta;
import net.weg.gedesti.model.service.ComissaoService;
import net.weg.gedesti.model.service.PautaService;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
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

    @GetMapping("/page")
    public ResponseEntity<Page<Pauta>> findAll(@PageableDefault(page = 9, size = 8, direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.FOUND).body(pautaService.findAll(pageable));
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid PautaDTO pautaDTO) {
        Pauta pauta = new Pauta();
        BeanUtils.copyProperties(pautaDTO, pauta);
//        Pauta pautaSalva = pautaService.save(pauta);
//        List<Comissao> comissao =pautaDTO.getFuncionarios();
//
//        for (Comissao comissoes : comissao) {
//            comissoes.setCodigoPauta(pautaSalva);
//            comissaoService.save(comissoes);
//        }

        return ResponseEntity.status(HttpStatus.CREATED).body(pautaService.save(pauta));
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Object> findById(@PathVariable(value = "codigo") Integer codigo) {
        Optional<Pauta> pautaOptional = pautaService.findById(codigo);

        if (pautaOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro nenhuma pauta com o codigo: " + codigo);
        }

        List<Comissao> comissaoList = comissaoService.findAll();
        List<Pauta> pautaLista = new ArrayList<>();
        pautaLista.add(pautaOptional.get());

//        for (Comissao comissao : comissaoList) {
//            for (Pauta pauta : pautaLista) {
//                if (comissao.getCodigoPauta().getCodigoPauta() == pauta.getCodigoPauta()) {
//                    pauta.getFuncionarios().add(comissao.getCodigoFuncionario());
//                }
//            }
//        }

        return ResponseEntity.status(HttpStatus.FOUND).body(pautaLista);
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<Object> deleteById(@PathVariable(value = "codigo") Integer codigo) {
        Optional<Pauta> pautaOptional = pautaService.findById(codigo);
        if (pautaOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro nenhuma pauta com o codigo: " + codigo);
        }
        pautaService.deleteById(codigo);
        return ResponseEntity.status(HttpStatus.FOUND).body("Pauta " + codigo + " deletada com sucesso!");
    }
}
