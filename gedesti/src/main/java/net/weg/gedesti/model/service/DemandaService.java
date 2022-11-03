package net.weg.gedesti.model.service;

import lombok.AllArgsConstructor;
import net.weg.gedesti.model.entity.Demanda;
import net.weg.gedesti.repository.DemandaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DemandaService {

    private DemandaRepository demandaRepository;

    public List<Demanda> findAll() {
        return demandaRepository.findAll();
    }

    public <S extends Demanda> S save(S entity) {
        return demandaRepository.save(entity);
    }

    public Optional<Demanda> findById(Integer integer) {
        return demandaRepository.findById(integer);
    }

    public boolean existsById(Integer integer) {
        return demandaRepository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        demandaRepository.deleteById(integer);
    }
}
