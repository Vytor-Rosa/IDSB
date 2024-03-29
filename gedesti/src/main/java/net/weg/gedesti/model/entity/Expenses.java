package net.weg.gedesti.model.entity;

import lombok.*;
import net.minidev.json.annotate.JsonIgnore;

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
    @JoinColumn(name = "proposal_code")
    @JsonIgnore
    private Proposal proposal;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "expenses")
    private List<ExpensesCostCenters> expensesCostCenters;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "expenses_code")
    private List<Expense> expense;
}
