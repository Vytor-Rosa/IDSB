package net.weg.gedesti.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "demanda")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Demanda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Integer codigoDemanda;

    @Column(nullable = false)
    private String tituloDemanda;

    @Column(nullable = false)
    private String problemaAtual;

    @Column(nullable = false)
    private String objetivoDemanda;

    @Column(nullable = false)
    private String statusDemanda;

    @Column(nullable = false)
    private Double score;

    @Column(nullable = false)
    private String periodoDeExecucao;

    @ManyToOne
    private Funcionario matriculaSolicitante;

    @OneToOne
    private BeneficioReal beneficioReal;

    @OneToOne
    private BeneficioQualitativo beneficioQualitativo;

    @OneToOne
    private BeneficioPotencial beneficioPotencial;

    @OneToMany(mappedBy = "codigoCentroDeCusto")
    List<CentroDemanda> centroDeCusto;
}
