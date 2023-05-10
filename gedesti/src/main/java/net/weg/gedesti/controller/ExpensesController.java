package net.weg.gedesti.controller;

import lombok.AllArgsConstructor;
import net.weg.gedesti.dto.ExpensesDTO;
import net.weg.gedesti.model.entity.Expenses;
import net.weg.gedesti.model.entity.Proposal;
import net.weg.gedesti.model.service.ExpensesService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
@RequestMapping("/api/expenses")
public class ExpensesController {
    private ExpensesService expensesService;

    @GetMapping
    public ResponseEntity<List<Expenses>> findAll() {
        return ResponseEntity.status(HttpStatus.FOUND).body(expensesService.findAll());
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid ExpensesDTO expensesDTO) {
        Expenses expenses = new Expenses();
        BeanUtils.copyProperties(expensesDTO, expenses);
        return ResponseEntity.status(HttpStatus.FOUND).body(expensesService.save(expenses));
    }

    @GetMapping("/{expensesCode}")
    public ResponseEntity<Object> findById(@PathVariable(value = "expensesCode") Integer integer) {
        if(expensesService.findById(integer).isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error! no expenses with code: " + integer);
        }

        return ResponseEntity.status(HttpStatus.FOUND).body(expensesService.findById(integer));
    }

    @GetMapping("/proposal/{proposalCode}")
    public ResponseEntity<Object> findAllByProposal(@PathVariable(value = "proposalCode") Proposal proposal) {
        if(expensesService.findAllByProposal(proposal).isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error! no expenses with code: " + proposal);
        }

        return ResponseEntity.status(HttpStatus.FOUND).body(expensesService.findAllByProposal(proposal));
    }
}
