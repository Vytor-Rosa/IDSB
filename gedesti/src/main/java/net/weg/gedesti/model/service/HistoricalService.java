package net.weg.gedesti.model.service;

import lombok.AllArgsConstructor;
import net.weg.gedesti.model.entity.Historico;
import net.weg.gedesti.repository.HistoricoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class HistoricalService {

    private HistoricoRepository historicoRepository;

    public List<Historico> findAll() {
        return historicoRepository.findAll();
    }

    public <S extends Historico> S save(S entity) {
        return historicoRepository.save(entity);
    }

    public Optional<Historico> findById(Integer integer) {
        return historicoRepository.findById(integer);
    }

    public boolean existsById(Integer integer) {
        return historicoRepository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        historicoRepository.deleteById(integer);
    }
}
