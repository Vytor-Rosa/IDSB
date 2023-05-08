package net.weg.gedesti.dto;

import lombok.Data;
import net.weg.gedesti.model.entity.CostCenter;
import net.weg.gedesti.model.entity.Expense;
import net.weg.gedesti.model.entity.Proposal;

import java.util.List;

@Data
public class ExpensesDTO {
    private Proposal proposal;
    private List<CostCenter> costCenter;
    private List<Expense> expense;
}
