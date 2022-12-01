package net.weg.gedesti.model.service;

import lombok.AllArgsConstructor;
import net.weg.gedesti.model.entity.CostCenter;
import net.weg.gedesti.repository.CentroDeCustoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CostCenterService {
    private CentroDeCustoRepository centroDeCustoRepository;

    public List<CostCenter> findAll() {
        return centroDeCustoRepository.findAll();
    }

    public <S extends CostCenter> S save(S entity) {
        return centroDeCustoRepository.save(entity);
    }

    public Optional<CostCenter> findById(Integer integer) {
        return centroDeCustoRepository.findById(integer);
    }

    public void deleteById(Integer integer) {
        centroDeCustoRepository.deleteById(integer);
    }
}
