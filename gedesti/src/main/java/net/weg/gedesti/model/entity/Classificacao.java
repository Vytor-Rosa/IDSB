package net.weg.gedesti.model.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

@Entity
@Table(name = "classificacao")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Classificacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Integer codigoClassificacao;

    @Column(nullable = false)
    private Integer tamanhoClassificacao;

    @Column(nullable = false)
    private String secaoTI;

    @Column(nullable = false)
    private Integer codigoPPM;

    @Column(nullable = false)
    private String linkEpicJira;

    @OneToOne
    private Bu buSolicitante;

    @ManyToMany
    @JoinTable(name = "busBeneficiadas",
            joinColumns = @JoinColumn(name = "codigoBu"),
            inverseJoinColumns = @JoinColumn(name = "codigoClassificacao"))
    private List<Bu> busBeneficiadas;

    @ManyToOne
    private Funcionario matriculaAnalista;
}
