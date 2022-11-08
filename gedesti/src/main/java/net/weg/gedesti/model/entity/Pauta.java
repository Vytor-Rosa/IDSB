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
    private Integer codigoPauta;
    @Column(nullable = false, unique = true)
    private Integer numeroSequencial;
    @Column(nullable = false)
    private Year anoPauta;

    @ManyToMany
    @JoinTable(name = "comissao",
            joinColumns = @JoinColumn(name = "codigoFuncionario"),
            inverseJoinColumns = @JoinColumn(name = "codigoPauta"))
    List<Funcionario> comissao;

//    @ManyToOne(cascade=CascadeType.ALL)
//    private Anexo anexo;

}
