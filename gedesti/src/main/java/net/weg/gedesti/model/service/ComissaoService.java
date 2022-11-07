package net.weg.gedesti.model.service;

import lombok.AllArgsConstructor;
import net.weg.gedesti.model.entity.Comissao;
import net.weg.gedesti.model.entity.Funcionario;
import net.weg.gedesti.model.entity.Pauta;
import net.weg.gedesti.repository.ComissaoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ComissaoService {
    ComissaoRepository comissaoRepository;

    public List<Comissao> findAll() {
        return comissaoRepository.findAll();
    }

    public <S extends Comissao> S save(S entity) {
        return comissaoRepository.save(entity);
    }

    public Optional<Comissao> findById(Integer integer) {
        return comissaoRepository.findById(integer);
    }

//    public List<Object> findByPauta(Pauta pauta) {
//        return comissaoRepository.findByPauta(pauta);
//    }

    public void deleteById(Integer integer) {
        comissaoRepository.deleteById(integer);
    }
}
