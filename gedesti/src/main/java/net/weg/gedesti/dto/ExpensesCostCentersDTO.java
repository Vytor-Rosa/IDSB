package net.weg.gedesti.dto;

import lombok.Data;
import net.weg.gedesti.model.entity.CostCenter;
import net.weg.gedesti.model.entity.Expenses;

@Data
public class ExpensesCostCentersDTO {
    private CostCenter costCenter;
    private Double percent;
}
