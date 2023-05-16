package net.weg.gedesti.model.service;

import lombok.AllArgsConstructor;
import net.weg.gedesti.model.entity.ExpensesCostCenters;
import net.weg.gedesti.repository.ExpensesCostCentersRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ExpensesCostCentersService {
    ExpensesCostCentersRepository expensesCostCentersRepository;

    public <S extends ExpensesCostCenters> S save(S entity) {
        return expensesCostCentersRepository.save(entity);
    }

    public Optional<ExpensesCostCenters> findById(Integer integer) {
        return expensesCostCentersRepository.findById(integer);
    }
}
