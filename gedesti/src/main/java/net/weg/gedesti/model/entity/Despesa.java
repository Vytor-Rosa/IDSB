package net.weg.gedesti.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "despesa")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Despesa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Integer codigoDespesa;

    @Column(nullable = false)
    private String tipoDespesa;

    @Column(nullable = false)
    private String perfilDespesa;

    @Column(nullable = false)
    private Integer periodoExecucao;

    @Column(nullable = false)
    private String quantidadeHoras;

    @Column(nullable = false)
    private Double valorHora;

    @Column(nullable = false)
    private Double valorTotal;

    @ManyToOne
    @JoinColumn(name = "codigo_centro_de_custo")
    private CentroDeCusto centroDeCusto;

//    @ManyToOne
//    @JoinColumn(name = "codigo_proposta")
//    private Proposta proposta;
}
