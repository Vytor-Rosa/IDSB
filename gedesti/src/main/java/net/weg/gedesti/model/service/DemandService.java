package net.weg.gedesti.model.service;

import lombok.AllArgsConstructor;
import net.weg.gedesti.model.entity.Demand;
import net.weg.gedesti.model.entity.Worker;
import net.weg.gedesti.repository.DemandRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
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


    public Page<Demand> findAllByActiveVersionOrderByScoreDesc(Pageable pageable) {
        return demandRepository.findAllByActiveVersionOrderByScoreDesc(pageable);
    }

    public List<Demand> findByDemandCode(Integer integer) {
        return demandRepository.findByDemandCode(integer);
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

    public List<Demand> findByDemandStatus(String status) {
        return demandRepository.findByDemandStatus(status);
    }

    public List<Demand> findAllByRequesterRegistration(Worker worker) {
        return demandRepository.findAllByRequesterRegistration(worker);
    }
}
