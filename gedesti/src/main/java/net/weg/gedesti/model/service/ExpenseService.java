package net.weg.gedesti.model.service;

import lombok.AllArgsConstructor;
import net.weg.gedesti.model.entity.Expense;
import net.weg.gedesti.repository.ExpenseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class  ExpenseService {
    private ExpenseRepository expenseRepository;

    public List<Expense> findAll() {
        return expenseRepository.findAll();
    }

    public <S extends Expense> S save(S entity) {
        return expenseRepository.save(entity);
    }

    public Optional<Expense> findById(Integer integer) {
        return expenseRepository.findById(integer);
    }

    public boolean existsById(Integer integer) {
        return expenseRepository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        expenseRepository.deleteById(integer);
    }
}
