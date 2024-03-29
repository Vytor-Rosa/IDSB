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

    private HistoricalRepository historicalRepository;

    public List<Historical> findAll() {
        return historicalRepository.findAll();
    }

    public <S extends Historical> S save(S entity) {
        return historicalRepository.save(entity);
    }

    public Optional<Historical> findById(Integer integer) {
        return historicalRepository.findById(integer);
    }

    public boolean existsById(Integer integer) {
        return historicalRepository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        historicalRepository.deleteById(integer);
    }
}
