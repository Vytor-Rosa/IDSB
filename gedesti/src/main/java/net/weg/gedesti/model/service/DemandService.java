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
        return demandRepository.findAllByActiveVersionOrderByScoreDesc();
    }

    public Page<Demand> findAll(Pageable pageable) {
        return demandRepository.findAll(pageable);
    }

    public List<Demand> findAllByDemandCode(Integer integer){
        return demandRepository.findAllByDemandCode(integer);
    }

    public Page<Demand> findAllByActiveVersionOrderByScoreDesc(Pageable pageable) {
        return demandRepository.findAllByActiveVersionOrderByScoreDesc(pageable);
    }

    public Page<Demand> findAllByActiveVersionAndDemandStatusOrderByScoreDesc(Pageable pageable) {
        return demandRepository.findAllByActiveVersionAndDemandStatusOrderByScoreDesc(pageable);
    }

    public Page<Demand> findAllByActiveVersionAndDepartmentOrderByScoreDesc(String department, Pageable pageable) {
        return demandRepository.findAllByActiveVersionAndDepartmentOrderByScoreDesc(department, pageable);
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

    public List<Demand> findAllByApprover(Worker worker){
        return demandRepository.findAllByApprover(worker);
    }

    public List<Demand> findAllByClassificationByAnalystRegistration(Worker worker){
        return demandRepository.findAllByClassificationAnalistRegistry(worker);
    }

    public List<Demand> findAllByVersion(Integer integer){
        return demandRepository.findAllByDemandVersion(integer);
    }

    public Demand findByDemandCodeAndDemandVersion(Integer demandCode, Integer demandVersion) {
        return demandRepository.findByDemandCodeAndDemandVersion(demandCode, demandVersion);
    }
}
