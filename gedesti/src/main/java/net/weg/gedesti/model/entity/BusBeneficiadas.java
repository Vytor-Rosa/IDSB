package net.weg.gedesti.model.entity;

import javax.persistence.*;

public class BusBeneficiadas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Integer beneficiariesBusCode;

    @ManyToOne
    @JoinColumn(name = "codigoBu")
    private Pauta buCode;

    @ManyToOne
    @JoinColumn(name = "codigoClassficacao")
    private Funcionario codigoClassficacao;
}
