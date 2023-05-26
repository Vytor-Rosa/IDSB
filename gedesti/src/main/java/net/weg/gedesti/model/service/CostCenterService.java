package net.weg.gedesti.model.service;

import lombok.AllArgsConstructor;
import net.weg.gedesti.model.entity.CostCenter;
import net.weg.gedesti.repository.CostCenterRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CostCenterService {
    private CostCenterRepository costCenterRepository;

    public List<CostCenter> findAll() {
        return costCenterRepository.findAll();
    }

    public <S extends CostCenter> S save(S entity) {
        return costCenterRepository.save(entity);
    }

    public Optional<CostCenter> findById(Integer integer) {
        return costCenterRepository.findById(integer);
    }

    public Optional<CostCenter> findByCostCenter(String costCenter) {
        return costCenterRepository.findByCostCenter(costCenter);
    }


    public void deleteById(Integer integer) {
        costCenterRepository.deleteById(integer);
    }

    public Object saveAndFlush(CostCenter costCenter) {
        return costCenterRepository.saveAndFlush(costCenter);
    }


}
