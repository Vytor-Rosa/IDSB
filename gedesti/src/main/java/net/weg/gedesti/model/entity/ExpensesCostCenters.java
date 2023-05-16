package net.weg.gedesti.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "expensesCostCenters")
public class ExpensesCostCenters {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer expensesCostCentersCode;

    @ManyToOne
    @JoinColumn(name = "costcenter_code")
    private CostCenter costCenter;

    @ManyToOne
    @JoinColumn(name = "expenses_code")
    @JsonIgnore
    private Expenses expenses;

    private Double percent;
}
