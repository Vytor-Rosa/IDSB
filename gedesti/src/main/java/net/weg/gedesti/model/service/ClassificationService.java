package net.weg.gedesti.model.service;

import lombok.AllArgsConstructor;
import net.weg.gedesti.model.entity.Classification;
import net.weg.gedesti.repository.ClassificacaoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ClassificationService {

    ClassificacaoRepository classificacaoRepository;

    public List<Classification> findAll() {
        return classificacaoRepository.findAll();
    }

    public <S extends Classification> S save(S entity) {
        return classificacaoRepository.save(entity);
    }

    public Optional<Classification> findById(Integer integer) {
        return classificacaoRepository.findById(integer);
    }

    public boolean existsById(Integer integer) {
        return classificacaoRepository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        classificacaoRepository.deleteById(integer);
    }
}
