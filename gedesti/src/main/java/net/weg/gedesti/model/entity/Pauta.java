package net.weg.gedesti.model.entity;

import lombok.*;

import javax.persistence.*;
import java.time.Year;
import java.util.List;

@Entity
@Table(name = "pauta")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Pauta {

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
    List<Funcionario> commission;

//    @ManyToOne(cascade=CascadeType.ALL)
//    private Anexo anexo;

}
