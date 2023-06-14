package net.weg.gedesti.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "agenda")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Agenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Integer agendaCode;

    @Column(nullable = false)
    private Integer sequentialNumber;

    @Column(nullable = false)
    private String initialDate;

    @Column(nullable = false)
    private String finalDate;

    @Column(nullable = false)
    private String agendaDate;

    @JsonIgnore
    @OneToMany(mappedBy = "agenda")
    private List<Minute> minutes;

    @ManyToOne
    @JoinTable(name = "agenda_commission",
            joinColumns = @JoinColumn(name = "agendaCode"),
            inverseJoinColumns = @JoinColumn(name = "commissionCode"))
    private Commission commission;

    @ManyToMany
    @JoinTable(name = "agenda_proposal",
            joinColumns = @JoinColumn(name = "agendaCode"),
            inverseJoinColumns = @JoinColumn(name = "proposalCode"))
    List<Proposal> proposals;

    @ManyToOne
    private Worker analistRegistry;
}
