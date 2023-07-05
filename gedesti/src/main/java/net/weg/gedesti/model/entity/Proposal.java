package net.weg.gedesti.model.entity;

import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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

    @Size(max = 9999)
    @Column
    private String descriptiveProposal;

    @OneToOne
    @JoinColumn
    private Worker responsibleAnalyst;

    @JoinColumns({
            @JoinColumn(name = "demandCode", referencedColumnName = "demandCode"),
            @JoinColumn(name = "demandVersion", referencedColumnName = "demandVersion")
    })
    @OneToOne
    private Demand demand;

    @Column(nullable = false)
    private Double totalCosts;

    @Column(nullable = false)
    private Double externalCosts;

    @Column(nullable = false)
    private Double internalCosts;

    @ManyToMany
    @JoinTable(name = "responsible_for_business",
            joinColumns = @JoinColumn(name = "proposalCode"),
            inverseJoinColumns = @JoinColumn(name = "workerCode"))
    List<Worker> workers;

    @NotNull
    private String proposalDate;

    @Column(nullable = true, length = 5000)
    private String commissionOpinion;

    @Column(nullable = true)
    private Boolean published;

    @OneToMany(mappedBy = "proposal", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Expenses> expensesList;

    @Column(nullable = true)
    private String dgOpinion;

    private Double score;
}
