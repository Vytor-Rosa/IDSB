package net.weg.gedesti.model.service;

import lombok.AllArgsConstructor;
import net.weg.gedesti.model.entity.CentroDeCusto;
import net.weg.gedesti.repository.CentroDeCustoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CostCenterService {
    private CentroDeCustoRepository centroDeCustoRepository;

    public List<CentroDeCusto> findAll() {
        return centroDeCustoRepository.findAll();
    }

    public <S extends CentroDeCusto> S save(S entity) {
        return centroDeCustoRepository.save(entity);
    }

    public Optional<CentroDeCusto> findById(Integer integer) {
        return centroDeCustoRepository.findById(integer);
    }

    public void deleteById(Integer integer) {
        centroDeCustoRepository.deleteById(integer);
    }
}
