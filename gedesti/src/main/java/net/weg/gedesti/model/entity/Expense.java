package net.weg.gedesti.model.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "expense")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Integer expenseCode;

    @Column(nullable = false)
    private String expenseType;

    @Column(nullable = false)
    private String expenseProfile;

    @Column(nullable = false)
    private String amountOfHours;

    @Column(nullable = false)
    private Double hourValue;

    @Column(nullable = false)
    private Double totalValue;

}
