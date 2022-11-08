package net.weg.gedesti.model.service;

import lombok.AllArgsConstructor;
import net.weg.gedesti.model.entity.Proposta;
import net.weg.gedesti.repository.PropostaRepository;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PropostaService {

    private PropostaRepository propostaRepository;

    public List<Proposta> findAll() {
        return propostaRepository.findAll();
    }

    public <S extends Proposta> S save(S entity) {
        return propostaRepository.save(entity);
    }

    public Optional<Proposta> findById(Integer integer) {
        return propostaRepository.findById(integer);
    }

    public boolean existsById(Integer integer) {
        return propostaRepository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        propostaRepository.deleteById(integer);
    }

    

}
