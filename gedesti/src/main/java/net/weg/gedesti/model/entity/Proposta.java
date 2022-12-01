package net.weg.gedesti.model.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

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
    private Integer proposalCode;

    @Column(nullable = false)
    private String proposalName;

    @Column(nullable = false)
    private String proposalStatus;

    @Column(nullable = false)
    private Double payback;

    @Column(nullable = false)
    private Date initialRunPeriod;

    @Column(nullable = false)
    private Date finalExecutionPeriod;

    @Column
    private String descriptiveProposal;

    @OneToOne
    @JoinColumn
    private Funcionario responsibleAnalyst;

    @ManyToOne
    private Pauta agendaCode;

    @ManyToMany
    @JoinTable(name = "responsaveis_pelo_negocio",
            joinColumns = @JoinColumn(name = "codigoFuncionario"),
            inverseJoinColumns = @JoinColumn(name = "codigoProposta"))
    List<Funcionario> Funcionarios;
}
