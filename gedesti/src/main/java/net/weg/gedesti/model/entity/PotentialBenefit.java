package net.weg.gedesti.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "beneficioPotencial")
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class PotentialBenefit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Integer potentialBenefitCode;

    @Column(nullable = false)
    private Double potentialMonthlyValue;

    @Column(nullable = false)
    private Boolean legalObrigation;

    @Column(nullable = false)
    private String potentialCurrency;

}