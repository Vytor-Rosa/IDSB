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
    private Integer qualitativeBenefitCode;

    @Column(nullable = false)
    private String frequencyOfUse;

    @Column(nullable = false)
    private boolean interalControlsRequirements;

}
