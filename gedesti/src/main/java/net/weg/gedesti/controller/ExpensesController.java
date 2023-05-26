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
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @GetMapping("/{expensesCode}")
    public ResponseEntity<Object> findById(@PathVariable(value = "expensesCode") Integer integer) {
        if (expensesService.findById(integer).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error! no expenses with code: " + integer);
        }

        return ResponseEntity.status(HttpStatus.FOUND).body(expensesService.findById(integer));
    }

    @GetMapping("/proposal/{proposalCode}")
    public ResponseEntity<Object> findAllByProposalProposalCode(@PathVariable(value = "proposalCode") Integer proposal_code) {
        if (expensesService.findAllByProposalProposalCode(proposal_code).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error! no expenses with code: " + proposal_code);
        }
        return ResponseEntity.status(HttpStatus.FOUND).body(expensesService.findAllByProposalProposalCode(proposal_code));
    }

//    @Modifying
//    @Transactional
//    @PutMapping("/{expensesCode}")
//    public ResponseEntity<Object> update(@PathVariable(value = "expensesCode") Integer expensesCode, @RequestBody @Valid ExpensesDTO ExpensesDTO) {
//        Optional<Expenses> expensesOptional = expensesService.findById(expensesCode);
//
//        if (expensesOptional.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error! No potential expense with code:" + expensesCode);
//        }
//        Expenses expenses = expensesOptional.get();
//        BeanUtils.copyProperties(ExpensesDTO, expenses);
//
//        List<ExpensesCostCenters> expensesCostCentersList = expenses.getExpensesCostCenters();
//        List<Expense> expenseList = expenses.getExpense();
//
//        expenses.setExpensesCostCenters(null);
//
//        List<ExpensesCostCenters> expensesCostCentersListRemove = expensesCostCentersRepository.findAllByExpenses(expensesOptional.get());
//        expensesCostCentersRepository.deleteAllInBatch(expensesCostCentersListRemove);
//
//        Expenses finalExpenses = expensesService.save(expenses);
//
//        List<ExpensesCostCenters> expensesCostCentersListSave = new ArrayList<>();
//        List<Expense> expenseListSave = new ArrayList<>();
//
//        if (expensesCostCentersList != null) {
//            for (ExpensesCostCenters expensesCostCenters : expensesCostCentersList) {
//                expensesCostCenters.setExpenses(finalExpenses);
//                expensesCostCentersListSave.add(expensesCostCentersRepository.saveAndFlush(expensesCostCenters));
//            }
//        } else {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("OUT");
//        }
//
//        if (expenseList != null) {
//            for (Expense expense : expenseList) {
//                expenseListSave.add(expenseRepository.save(expense));
//            }
//        } else {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("OUT");
//        }
//
//        finalExpenses.setExpense(expenseListSave);
//
//        return ResponseEntity.status(HttpStatus.OK).body(expensesService.saveAndFlush(finalExpenses));
//    }


    @Modifying
    @Transactional
    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable("id") Integer id, @RequestBody @Valid ExpensesDTO expensesDTO) {
        Optional<Expenses> optionalExpenses = expensesService.findById(id);

        if (optionalExpenses.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error! No expenses with code: " + id);
        }

        Expenses existingExpenses = optionalExpenses.get();

        // Atualiza os atributos existentes com os valores do DTO
        BeanUtils.copyProperties(expensesDTO, existingExpenses, "expensesCostCenters");

        // Remove os expensesCostCenters que foram removidos
        List<ExpensesCostCenters> removedExpensesCostCenters = new ArrayList<>();

        if (existingExpenses.getExpensesCostCenters() != null) {
            List<ExpensesCostCenters> updatedExpensesCostCenters = new ArrayList<>();

            for (ExpensesCostCenters existingCostCenter : existingExpenses.getExpensesCostCenters()) {
                boolean found = false;

                if (existingCostCenter.getExpensesCostCentersCode() != null) {
                    for (ExpensesCostCenters dtoCostCenter : expensesDTO.getExpensesCostCenters()) {
                        if (existingCostCenter.getExpensesCostCentersCode().equals(dtoCostCenter.getExpensesCostCentersCode())) {
                            found = true;
                            updatedExpensesCostCenters.add(dtoCostCenter);
                            break;
                        }
                    }
                }

                if (!found) {
                    removedExpensesCostCenters.add(existingCostCenter);
                }
            }

            existingExpenses.setExpensesCostCenters(updatedExpensesCostCenters);
        } else {
            // Remove todos os expensesCostCenters existentes, pois o DTO não possui nenhum
            existingExpenses.setExpensesCostCenters(new ArrayList<>());
        }

        // Adiciona novos expensesCostCenters
        if (expensesDTO.getExpensesCostCenters() != null) {
            for (ExpensesCostCenters dtoCostCenter : expensesDTO.getExpensesCostCenters()) {
                boolean found = false;

                if (dtoCostCenter.getExpensesCostCentersCode() != null) {
                    for (ExpensesCostCenters existingCostCenter : existingExpenses.getExpensesCostCenters()) {
                        if (dtoCostCenter.getExpensesCostCentersCode().equals(existingCostCenter.getExpensesCostCentersCode())) {
                            found = true;
                            break;
                        }
                    }
                }

                if (!found) {
                    dtoCostCenter.setExpenses(existingExpenses);
                    existingExpenses.getExpensesCostCenters().add(dtoCostCenter);
                }
            }
        }

        Expenses updatedExpenses = expensesService.save(existingExpenses);

        // Remove os expensesCostCenters removidos do banco de dados
        for (ExpensesCostCenters removedCostCenter : removedExpensesCostCenters) {
            expensesCostCentersRepository.delete(removedCostCenter);
        }

        return ResponseEntity.status(HttpStatus.OK).body(updatedExpenses);
    }


}
