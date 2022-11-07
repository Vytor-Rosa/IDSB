package net.weg.gedesti.model.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "CentroDeCustoDaDemanda")
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class CentroDemanda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Integer codigoCentroDemanda;

    @Column(nullable = false)
    private Integer codigoDemanda;

    @ManyToOne
    @JoinColumn(name = "codigo_centro_de_custo")
    private CentroDeCusto codigoCentroDeCusto;

}