package net.weg.gedesti.model.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "realbenefit")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RealBenefit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Integer realBenefitCode;

    @Column(nullable = false)
    private Double realMonthlyValue;

    @Size(max = 9999)
    @Column(nullable = false)
    private String realBenefitDescription;

    @Column(nullable = false)
    private String realCurrency;
}
