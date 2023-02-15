package net.weg.gedesti.model.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "qualitativebenefit")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class QualitativeBenefit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Integer qualitativeBenefitCode;

    @Column(nullable = false)
    private String frequencyOfUse;

    @Size(max = 5000)
    @Column(nullable = false)
    private String qualitativeBenefitDescription;

    @Column(nullable = false)
    private boolean interalControlsRequirements;

}
