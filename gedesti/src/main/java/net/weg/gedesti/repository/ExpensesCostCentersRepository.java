package net.weg.gedesti.repository;

import net.weg.gedesti.model.entity.Expenses;
import net.weg.gedesti.model.entity.ExpensesCostCenters;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpensesCostCentersRepository extends JpaRepository<ExpensesCostCenters, Integer> {

    List<ExpensesCostCenters> findAllByExpenses(Expenses expenses);

}
