package net.weg.gedesti.repository;

import net.weg.gedesti.model.entity.Expenses;
import net.weg.gedesti.model.entity.Proposal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExpensesRepository extends JpaRepository<Expenses, Integer> {
    List<Expenses> findAllByProposal(Proposal proposal);
}
