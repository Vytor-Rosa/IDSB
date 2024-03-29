package net.weg.gedesti.model.service;
//L
import lombok.AllArgsConstructor;
import net.weg.gedesti.model.entity.Expenses;
import net.weg.gedesti.model.entity.Proposal;
import net.weg.gedesti.repository.ExpensesRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ExpensesService {
    private ExpensesRepository expensesRepository;

    public List<Expenses> findAll() {
        return expensesRepository.findAll();
    }

    public <S extends Expenses> S save(S entity) {
        return expensesRepository.save(entity);
    }

    public Optional<Expenses> findById(Integer integer) {
        return expensesRepository.findById(integer);
    }

    public List<Expenses> findAllByProposal(Proposal proposal) {
        return expensesRepository.findAllByProposal(proposal);
    }

    public Object saveAndFlush(Expenses finalExpenses) {
        return expensesRepository.saveAndFlush(finalExpenses);
    }

    public List<Expenses> findAllByProposalProposalCode(Integer proposalCode) {
        return expensesRepository.findAllByProposalProposalCode(proposalCode);
    }
}
