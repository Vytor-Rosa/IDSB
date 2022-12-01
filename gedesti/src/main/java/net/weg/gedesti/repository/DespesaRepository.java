package net.weg.gedesti.repository;

import net.weg.gedesti.model.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DespesaRepository extends JpaRepository<Expense, Integer> {
}
