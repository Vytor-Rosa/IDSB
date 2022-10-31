package net.weg.gedesti.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "beneficioQualitativo")
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class BeneficioQualitativo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Integer codigoBeneficioQualitativo;

    @Column(nullable = false)
    private String frequeciaDeUso;

    @Column(nullable = false)
    private boolean requisitosControlesInternos;

}
