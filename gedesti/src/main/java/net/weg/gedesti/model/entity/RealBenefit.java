package net.weg.gedesti.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "realbenefit")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RealBenefit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Integer realBenefitCode;

    @Column(nullable = false)
    private Double realMonthlyValue;

    @Column(nullable = false)
    private String realBenefitDescription;

    @Column(nullable = false)
    private String realCurrency;
}
