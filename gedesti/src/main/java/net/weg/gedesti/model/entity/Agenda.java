package net.weg.gedesti.model.entity;

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
    private Integer yearAgenda;

    @OneToMany(cascade = CascadeType.ALL)
    List<Commission> commission;

    @Column(nullable = false)
    private String agendaDate;


    @ManyToMany
    @JoinTable(name = "agenda_proposal",
            joinColumns = @JoinColumn(name = "agendaCode"),
            inverseJoinColumns = @JoinColumn(name = "proposalCode"))
    List<Proposal> proposals;


//    @ManyToOne(cascade=CascadeType.ALL)
//    private Anexo anexo;

}
