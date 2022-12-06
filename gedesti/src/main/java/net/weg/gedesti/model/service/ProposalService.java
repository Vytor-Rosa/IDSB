package net.weg.gedesti.model.service;

import lombok.AllArgsConstructor;
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

    private ProposalRepository propostaRepository;

    public List<Proposal> findAll() {
        return propostaRepository.findAll();
    }

    public Page<Proposal> findAll(Pageable pageable) {
        return propostaRepository.findAll(pageable);
    }

    public <S extends Proposal> S save(S entity) {
        return propostaRepository.save(entity);
    }

    public Optional<Proposal> findById(Integer integer) {
        return propostaRepository.findById(integer);
    }

    public boolean existsById(Integer integer) {
        return propostaRepository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        propostaRepository.deleteById(integer);
    }


}
