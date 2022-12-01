package net.weg.gedesti.model.service;

import lombok.AllArgsConstructor;
import net.weg.gedesti.model.entity.QualitativeBenefit;
import net.weg.gedesti.repository.BeneficioQualitativoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class QualitativeBenefitService {
    private BeneficioQualitativoRepository beneficioQualitativoRepository;

    public List<QualitativeBenefit> findAll() {
        return beneficioQualitativoRepository.findAll();
    }

    public <S extends QualitativeBenefit> S save(S entity) {
        return beneficioQualitativoRepository.save(entity);
    }

    public Optional<QualitativeBenefit> findById(Integer integer) {
        return beneficioQualitativoRepository.findById(integer);
    }

    public boolean existsById(Integer integer) {
        return beneficioQualitativoRepository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        beneficioQualitativoRepository.deleteById(integer);
    }
}
