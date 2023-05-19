package net.weg.gedesti.controller;

import lombok.AllArgsConstructor;
import net.weg.gedesti.dto.ExpensesDTO;
import net.weg.gedesti.model.entity.Expense;
import net.weg.gedesti.model.entity.Expenses;
import net.weg.gedesti.model.entity.ExpensesCostCenters;
import net.weg.gedesti.model.entity.Proposal;
import net.weg.gedesti.model.service.ExpensesService;
import net.weg.gedesti.repository.ExpenseRepository;
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
    private ExpensesCostCentersRepository expensesCostCentersRepository;
    private ExpenseRepository expenseRepository;

    @GetMapping
    public ResponseEntity<List<Expenses>> findAll() {
        return ResponseEntity.status(HttpStatus.FOUND).body(expensesService.findAll());
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid ExpensesDTO expensesDTO) {
        Expenses expenses = new Expenses();
        BeanUtils.copyProperties(expensesDTO, expenses);

        List<ExpensesCostCenters> expensesCostCentersList = expenses.getExpensesCostCenters();
        List<Expense> expenseList = expenses.getExpense();

        expenses.setExpensesCostCenters(null);

        Expenses finalExpenses = expensesService.save(expenses);

        List<ExpensesCostCenters> expensesCostCentersListSave = new ArrayList<>();
        List<Expense> expenseListSave = new ArrayList<>();

        if (expensesCostCentersList != null) {
            for (ExpensesCostCenters expensesCostCenters : expensesCostCentersList) {
                expensesCostCenters.setExpenses(finalExpenses);
                expensesCostCentersListSave.add(expensesCostCentersRepository.save(expensesCostCenters));
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("OUT");
        }

        if (expenseList != null) {
            for (Expense expense : expenseList) {
                expenseListSave.add(expenseRepository.save(expense));
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("OUT");
        }

        finalExpenses.setExpense(expenseListSave);
        return ResponseEntity.status(HttpStatus.FOUND).body(finalExpenses);
    }

//    @PutMapping
//    public ResponseEntity<Object> edit(@RequestBody @Valid ExpensesDTO expensesDTO) {
//        Expenses expenses = new Expenses();
//        BeanUtils.copyProperties(expensesDTO, expenses);
//    }

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
