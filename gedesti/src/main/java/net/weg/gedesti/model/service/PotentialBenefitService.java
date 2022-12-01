package net.weg.gedesti.model.service;

import lombok.AllArgsConstructor;
import net.weg.gedesti.model.entity.BeneficioPotencial;
import net.weg.gedesti.repository.BeneficioPotencialRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PotentialBenefitService {

    private BeneficioPotencialRepository beneficioPotencialRepository;

    public List<BeneficioPotencial> findAll() {
        return beneficioPotencialRepository.findAll();
    }

    public <S extends BeneficioPotencial> S save(S entity) {
        return beneficioPotencialRepository.save(entity);
    }

    public Optional<BeneficioPotencial> findById(Integer integer) {
        return beneficioPotencialRepository.findById(integer);
    }

    public boolean existsById(Integer integer) {
        return beneficioPotencialRepository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        beneficioPotencialRepository.deleteById(integer);
    }
}
