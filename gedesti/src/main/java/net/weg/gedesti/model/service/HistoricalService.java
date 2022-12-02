package net.weg.gedesti.model.service;

import lombok.AllArgsConstructor;
import net.weg.gedesti.model.entity.Historical;
import net.weg.gedesti.repository.HistoricalRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class HistoricalService {

    private HistoricalRepository historicoRepository;

    public List<Historical> findAll() {
        return historicoRepository.findAll();
    }

    public <S extends Historical> S save(S entity) {
        return historicoRepository.save(entity);
    }

    public Optional<Historical> findById(Integer integer) {
        return historicoRepository.findById(integer);
    }

    public boolean existsById(Integer integer) {
        return historicoRepository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        historicoRepository.deleteById(integer);
    }
}
