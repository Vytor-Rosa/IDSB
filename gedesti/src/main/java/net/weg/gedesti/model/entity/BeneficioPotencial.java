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
public class BeneficioPotencial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Integer codigoBeneficioPotencial;

    @Column(nullable = false)
    private Double valorMensalPotencial;

    @Column(nullable = false)
    private Boolean obrigacaoLegal;

    @Column(nullable = false)
    private String moedaPotencial;

}