package net.weg.gedesti.model.service;

import lombok.AllArgsConstructor;
import net.weg.gedesti.model.entity.Demand;
import net.weg.gedesti.repository.DemandRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DemandService {

    private DemandRepository demandaRepository;

    public List<Demand> findAll() {
        return demandaRepository.findAll();
    }

    public Page<Demand> findAll(Pageable pageable) {
        return demandaRepository.findAll(pageable);
    }

    public <S extends Demand> S save(S entity) {
        return demandaRepository.save(entity);
    }

    public Optional<Demand> findById(Integer integer) {
        return demandaRepository.findById(integer);
    }

    public boolean existsById(Integer integer) {
        return demandaRepository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        demandaRepository.deleteById(integer);
    }


}
