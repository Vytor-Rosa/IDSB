package net.weg.gedesti.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Year;
import java.util.List;

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

    @OneToMany(mappedBy = "codigoFuncionario")
    private List<Funcionario> funcionarios;
//    @ManyToOne(cascade=CascadeType.ALL)
//    private Anexo anexo;

}
