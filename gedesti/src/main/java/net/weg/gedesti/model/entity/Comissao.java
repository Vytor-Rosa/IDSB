package net.weg.gedesti.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "comissao")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Comissao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Integer codigoComissao;

    @ManyToOne
    @JoinColumn(name = "codigo_pauta")
    private Pauta codigoPauta;

    @ManyToOne
    @JoinColumn(name = "codigo_funcionario")
    private Funcionario codigoFuncionario;
}