package net.weg.gedesti.model.service;

import lombok.AllArgsConstructor;
import net.weg.gedesti.model.entity.Demand;
import net.weg.gedesti.model.entity.Worker;
import net.weg.gedesti.repository.DemandRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DemandService {

    private DemandRepository demandRepository;

    public List<Demand> findAll() {
        return demandRepository.findAll();
    }

    public Page<Demand> findAll(Pageable pageable) {
        return demandRepository.findAll(pageable);
    }

    public <S extends Demand> S save(S entity) {
        return demandRepository.save(entity);
    }

    public Optional<Demand> findById(Integer integer) {
        return demandRepository.findById(integer);
    }

    public boolean existsById(Integer integer) {
        return demandRepository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        demandRepository.deleteById(integer);
    }

    public Object saveAndFlush(Demand demand) {
        return demandRepository.saveAndFlush(demand);
    }

    public List<Demand> findByDemandStatus(String status) {
        return demandRepository.findByDemandStatus(status);
    }
}
