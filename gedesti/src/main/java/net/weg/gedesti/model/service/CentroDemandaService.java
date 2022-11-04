package net.weg.gedesti.model.service;

import lombok.AllArgsConstructor;
import net.weg.gedesti.model.entity.CentroDemanda;
import net.weg.gedesti.repository.CentroDemandaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CentroDemandaService {

    private CentroDemandaRepository centroDemandaRepository;

    public List<CentroDemanda> findAll() {
        return centroDemandaRepository.findAll();
    }

    public <S extends CentroDemanda> S save(S entity) {
        return centroDemandaRepository.save(entity);
    }

    public Optional<CentroDemanda> findById(Integer integer) {
        return centroDemandaRepository.findById(integer);
    }

    public boolean existsById(Integer integer) {
        return centroDemandaRepository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        centroDemandaRepository.deleteById(integer);
    }
}
