package net.weg.gedesti.model.entity;

import lombok.*;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "expenses")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class Expenses {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer expensesCode;

    @Column(nullable = false)
    private String expensesType;

    @ManyToOne
    private Proposal proposal;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "expenses")
    private List<ExpensesCostCenters> expensesCostCenters;

    @OneToMany
    @JoinColumn(name = "expense")
    private List<Expense> expense;
}
