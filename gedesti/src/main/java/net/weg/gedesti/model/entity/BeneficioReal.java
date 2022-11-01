package net.weg.gedesti.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "beneficioReal")
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
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
