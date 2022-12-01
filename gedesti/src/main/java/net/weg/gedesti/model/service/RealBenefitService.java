package net.weg.gedesti.model.service;

import lombok.AllArgsConstructor;
import net.weg.gedesti.model.entity.BeneficioReal;
import net.weg.gedesti.repository.BeneficioRealRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RealBenefitService {
    private BeneficioRealRepository beneficioRealRepository;

    public List<BeneficioReal> findAll() {
        return beneficioRealRepository.findAll();
    }

    public <S extends BeneficioReal> S save(S entity) {
        return beneficioRealRepository.save(entity);
    }

    public Optional<BeneficioReal> findById(Integer integer) {
        return beneficioRealRepository.findById(integer);
    }

    public boolean existsById(Integer integer) {
        return beneficioRealRepository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        beneficioRealRepository.deleteById(integer);
    }
}
