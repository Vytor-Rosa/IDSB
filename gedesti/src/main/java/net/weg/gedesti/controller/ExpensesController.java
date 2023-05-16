package net.weg.gedesti.controller;

import lombok.AllArgsConstructor;
import net.weg.gedesti.dto.ExpensesDTO;
import net.weg.gedesti.model.entity.Expenses;
import net.weg.gedesti.model.entity.ExpensesCostCenters;
import net.weg.gedesti.model.entity.Proposal;
import net.weg.gedesti.model.service.ExpensesCostCentersService;
import net.weg.gedesti.model.service.ExpensesService;
import net.weg.gedesti.repository.ExpensesCostCentersRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/api/expenses")
public class ExpensesController {
    private ExpensesService expensesService;
    private ExpensesCostCentersService expensesCostCentersService;
    private ExpensesCostCentersRepository expensesCostCentersRepository;

    @GetMapping
    public ResponseEntity<List<Expenses>> findAll() {
        return ResponseEntity.status(HttpStatus.FOUND).body(expensesService.findAll());
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid ExpensesDTO expensesDTO) {
        Expenses expenses = new Expenses();
        BeanUtils.copyProperties(expensesDTO, expenses);

        List<ExpensesCostCenters> expensesCostCentersList = expenses.getExpensesCostCenters();
        expenses.setExpensesCostCenters(null);

        Expenses finalExpenses = expensesService.save(expenses);

        List<ExpensesCostCenters> expensesCostCentersListSave = new ArrayList<>();

        if (expensesCostCentersList != null) {
            for (ExpensesCostCenters expensesCostCenters : expensesCostCentersList) {
                expensesCostCenters.setExpenses(finalExpenses);
                expensesCostCentersListSave.add(expensesCostCentersRepository.save(expensesCostCenters));
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("OUT");
        }

        return ResponseEntity.status(HttpStatus.FOUND).body(finalExpenses);
    }

    @GetMapping("/{expensesCode}")
    public ResponseEntity<Object> findById(@PathVariable(value = "expensesCode") Integer integer) {
        if (expensesService.findById(integer).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error! no expenses with code: " + integer);
        }

        return ResponseEntity.status(HttpStatus.FOUND).body(expensesService.findById(integer));
    }

    @GetMapping("/proposal/{proposalCode}")
    public ResponseEntity<Object> findAllByProposal(@PathVariable(value = "proposalCode") Proposal proposal) {
        if (expensesService.findAllByProposal(proposal).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error! no expenses with code: " + proposal);
        }

        return ResponseEntity.status(HttpStatus.FOUND).body(expensesService.findAllByProposal(proposal));
    }
}
