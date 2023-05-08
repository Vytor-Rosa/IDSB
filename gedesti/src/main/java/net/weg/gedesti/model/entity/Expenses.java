package net.weg.gedesti.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "expenses")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Expenses {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer expensesCode;
    @Column(nullable = false)
    private String expensesType;
    @ManyToOne
    private Proposal proposal;
    @ManyToMany
    private List<CostCenter> costCenter;
    @OneToMany
    private List<Expense> expense;
}
