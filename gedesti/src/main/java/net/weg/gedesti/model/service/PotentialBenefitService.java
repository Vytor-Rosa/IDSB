package net.weg.gedesti.model.service;

import lombok.AllArgsConstructor;
import net.weg.gedesti.model.entity.PotentialBenefit;
import net.weg.gedesti.repository.PotentialBenefitRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PotentialBenefitService {

    private PotentialBenefitRepository potentialBenefitRepository;

    public List<PotentialBenefit> findAll() {
        return potentialBenefitRepository.findAll();
    }

    public <S extends PotentialBenefit> S save(S entity) {
        return potentialBenefitRepository.save(entity);
    }

    public Optional<PotentialBenefit> findById(Integer integer) {
        return potentialBenefitRepository.findById(integer);
    }

    public boolean existsById(Integer integer) {
        return potentialBenefitRepository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        potentialBenefitRepository.deleteById(integer);
    }
}
