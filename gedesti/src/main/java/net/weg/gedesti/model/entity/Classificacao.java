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
    private Integer classificationCode;

    @Column(nullable = false)
    private Integer classificationSize;

    @Column(nullable = false)
    private String ITSection;

    @Column(nullable = false)
    private Integer PPMCode;

    @Column(nullable = false)
    private String EpicJiraLink;

    @OneToOne
    private Bu requesterBu;

    @ManyToMany
    @JoinTable(name = "busBeneficiadas",
            joinColumns = @JoinColumn(name = "codigoBu"),
            inverseJoinColumns = @JoinColumn(name = "codigoClassificacao"))
    private List<Bu> beneficiaryBu;

    @ManyToOne
    private Funcionario analistRegistry;
}
