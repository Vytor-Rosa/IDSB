package net.weg.gedesti.model.service;

import lombok.AllArgsConstructor;
import net.weg.gedesti.model.entity.Demand;
import net.weg.gedesti.model.entity.Proposal;
import net.weg.gedesti.repository.ProposalRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProposalService {

    private ProposalRepository proposalRepository;

    public List<Proposal> findAll() {
        return proposalRepository.findAll();
    }

    public Page<Proposal> findAll(Pageable pageable) {
        return proposalRepository.findAll(pageable);
    }

    public <S extends Proposal> S save(S entity) {
        return proposalRepository.save(entity);
    }

    public Optional<Proposal> findById(Integer integer) {
        return proposalRepository.findById(integer);
    }

    public Optional<Proposal> findByDemand(Demand demand) {
        return proposalRepository.findByDemand(demand);
    }

    public boolean existsById(Integer integer) {
        return proposalRepository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        proposalRepository.deleteById(integer);
    }

    public Optional<Proposal> findByDemandDemandCode(Integer demandCode){
        return proposalRepository.findByDemandDemandCode(demandCode);
    }
}
