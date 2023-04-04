package net.weg.gedesti.controller;

import lombok.AllArgsConstructor;
import net.weg.gedesti.dto.ExpenseDTO;
import net.weg.gedesti.model.entity.Expense;
import net.weg.gedesti.model.service.ExpenseService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api/expense")
public class ExpenseController {

    private ExpenseService expenseService;

    @GetMapping
    public ResponseEntity<List<Expense>> findAll() {
        return ResponseEntity.status(HttpStatus.FOUND).body(expenseService.findAll());
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid ExpenseDTO expenseDTO) {
        Expense expense = new Expense();
        BeanUtils.copyProperties(expenseDTO, expense);
        return ResponseEntity.status(HttpStatus.FOUND).body(expenseService.save(expense));
    }

    @GetMapping("/{expenseCode}")
    public ResponseEntity<Object> findById(@PathVariable(value = "expenseCode") Integer expenseCode) {
        Optional<Expense> expenseOptional = expenseService.findById(expenseCode);
        if (expenseOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error! no expense with code: " + expenseCode);
        }
        return ResponseEntity.status(HttpStatus.FOUND).body(expenseOptional);
    }

    @DeleteMapping("/{expenseCode}")
    public ResponseEntity<Object> deleteById(@PathVariable(value = "expenseCode") Integer expenseCode) {
        Optional<Expense> expenseOptional = expenseService.findById(expenseCode);
        if (expenseOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error! no expense with code:" + expenseCode);
        }
        expenseService.deleteById(expenseCode);
        return ResponseEntity.status(HttpStatus.FOUND).body("Expense " + expenseCode + " successfully deleted!");
    }
}
