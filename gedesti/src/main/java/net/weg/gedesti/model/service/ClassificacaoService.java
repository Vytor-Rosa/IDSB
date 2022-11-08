package net.weg.gedesti.model.service;

import lombok.AllArgsConstructor;
import net.weg.gedesti.model.entity.Classificacao;
import net.weg.gedesti.repository.ClassificacaoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ClassificacaoService {

    ClassificacaoRepository classificacaoRepository;

    public List<Classificacao> findAll() {
        return classificacaoRepository.findAll();
    }

    public <S extends Classificacao> S save(S entity) {
        return classificacaoRepository.save(entity);
    }

    public Optional<Classificacao> findById(Integer integer) {
        return classificacaoRepository.findById(integer);
    }

    public boolean existsById(Integer integer) {
        return classificacaoRepository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        classificacaoRepository.deleteById(integer);
    }
}
