package net.weg.gedesti.model.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "potentialbenefit")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PotentialBenefit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Integer potentialBenefitCode;

    @Column(nullable = false)
    private Double potentialMonthlyValue;

    @Column()
    private Boolean legalObrigation;

    @Size(max = 9999)
    @Column(nullable = false)
    private String potentialBenefitDescription;

    @Column(nullable = false)
    private String potentialCurrency;

}
