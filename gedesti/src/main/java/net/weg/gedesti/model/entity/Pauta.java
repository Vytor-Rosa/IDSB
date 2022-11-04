package net.weg.gedesti.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Year;

@Entity
@Table(name = "pauta")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Pauta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Integer codigoPauta;
    @Column(nullable = false, unique = true)
    private Integer numeroSequencial;
    @Column(nullable = false)
    private Year anoPauta;
//    @ManyToOne(cascade=CascadeType.ALL)
//    private Anexo anexo;

}
