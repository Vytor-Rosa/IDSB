package net.weg.gedesti.model.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "proposal")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Proposal {
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
    private Worker responsibleAnalyst;

    @ManyToOne
    private Agenda agendaCode;

    @ManyToMany
    @JoinTable(name = "responsible_for_business",
            joinColumns = @JoinColumn(name = "workerCode"),
            inverseJoinColumns = @JoinColumn(name = "proposalCode"))
    List<Worker> workers;
}
