package net.weg.gedesti.model.entity;

import javax.persistence.*;

@Entity
@Table(name = "beneficioReal")
public class BeneficioReal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Integer codigoBenficioReal;

    @Column(nullable = false)
    private Double valorMensalReal;

    @Column(nullable = false)
    private String descricaoBeneficioReal;

    @Column(nullable = false)
    private String moedaValorReal;
}
