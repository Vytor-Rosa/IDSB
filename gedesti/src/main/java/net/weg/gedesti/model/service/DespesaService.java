package net.weg.gedesti.model.service;

import lombok.AllArgsConstructor;
import net.weg.gedesti.model.entity.Despesa;
import net.weg.gedesti.repository.DespesaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DespesaService {
    private DespesaRepository despesaRepository;

    public List<Despesa> findAll() {
        return despesaRepository.findAll();
    }

    public <S extends Despesa> S save(S entity) {
        return despesaRepository.save(entity);
    }

    public Optional<Despesa> findById(Integer integer) {
        return despesaRepository.findById(integer);
    }

    public boolean existsById(Integer integer) {
        return despesaRepository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        despesaRepository.deleteById(integer);
    }
}
