package net.weg.gedesti.model.service;

import lombok.AllArgsConstructor;
import net.weg.gedesti.model.entity.QualitativeBenefit;
import net.weg.gedesti.repository.QualitativeBenefitRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class QualitativeBenefitService {
    private QualitativeBenefitRepository qualitativeBenefitRepository;

    public List<QualitativeBenefit> findAll() {
        return qualitativeBenefitRepository.findAll();
    }

    public <S extends QualitativeBenefit> S save(S entity) {
        return qualitativeBenefitRepository.save(entity);
    }

    public Optional<QualitativeBenefit> findById(Integer integer) {
        return qualitativeBenefitRepository.findById(integer);
    }

    public boolean existsById(Integer integer) {
        return qualitativeBenefitRepository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        qualitativeBenefitRepository.deleteById(integer);
    }
}