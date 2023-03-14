package net.weg.gedesti.model.service;

import lombok.AllArgsConstructor;
import net.weg.gedesti.model.entity.RealBenefit;
import net.weg.gedesti.repository.RealBenefitRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RealBenefitService {
    private RealBenefitRepository realBenefitRepository;

    public List<RealBenefit> findAll() {
        return realBenefitRepository.findAll();
    }

    public <S extends RealBenefit> S save(S entity) {
        return realBenefitRepository.save(entity);
    }

    public Optional<RealBenefit> findById(Integer integer) {
        return realBenefitRepository.findById(integer);
    }

    public boolean existsById(Integer integer) {
        return realBenefitRepository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        realBenefitRepository.deleteById(integer);
    }

    public Object saveAndFlush(RealBenefit realBenefit) {
        return realBenefitRepository.saveAndFlush(realBenefit);
    }
}
