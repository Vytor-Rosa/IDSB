package net.weg.gedesti.model.service;

import lombok.AllArgsConstructor;
import net.weg.gedesti.model.entity.BeneficioQualitativo;
import net.weg.gedesti.repository.BeneficioQualitativoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BeneficioQualitativoService{
    private BeneficioQualitativoRepository beneficioQualitativoRepository;

    public List<BeneficioQualitativo> findAll() {
        return beneficioQualitativoRepository.findAll();
    }

    public <S extends BeneficioQualitativo> S save(S entity) {
        return beneficioQualitativoRepository.save(entity);
    }

    public Optional<BeneficioQualitativo> findById(Integer integer) {
        return beneficioQualitativoRepository.findById(integer);
    }

    public boolean existsById(Integer integer) {
        return beneficioQualitativoRepository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        beneficioQualitativoRepository.deleteById(integer);
    }
}
