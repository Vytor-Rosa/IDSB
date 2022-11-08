package net.weg.gedesti.model.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "proposta")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Proposta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Integer codigoProposta;
    @Column(nullable = false)
    private String nomeProposta;
    @Column(nullable = false)
    private String statusProposta;
    @Column(nullable = false)
    private Double payback;
    @Column(nullable = false)
    private Date periodoExecucaoInicial;
    @Column(nullable = false)
    private Date periodoExecucaoFinal;
    @Column
    private String descritivoProposta;
    @OneToOne
    @JoinColumn(name = "codigo_demanda")
    private Demanda codigoDemanda;
    @OneToOne
    @JoinColumn
    private Funcionario analistaResponsavel;
    @ManyToOne
    private Pauta codigoPauta;
}
