package net.weg.gedesti.dto;

import lombok.Data;
import net.weg.gedesti.model.entity.CostCenter;
import net.weg.gedesti.model.entity.Expense;
import net.weg.gedesti.model.entity.ExpensesCostCenters;
import net.weg.gedesti.model.entity.Proposal;

import javax.persistence.Column;
import java.util.List;

@Data
public class ExpensesDTO {
    @Column(nullable = false)
    private String expensesType;
    private Proposal proposal;
    private List<ExpensesCostCenters> expensesCostCenters;
    private List<Expense> expense;
}
