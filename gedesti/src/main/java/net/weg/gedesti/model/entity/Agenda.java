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
    @Column(nullable = false, unique = true)
    private Integer sequentialNumber;
    @Column(nullable = false)
    private Integer yearAgenda;

    @ManyToMany
    @JoinTable(name = "commission",
            joinColumns = @JoinColumn(name = "workerCode"),
            inverseJoinColumns = @JoinColumn(name = "agendaCode"))
    List<Worker> commission;

//    @ManyToOne(cascade=CascadeType.ALL)
//    private Anexo anexo;

}
