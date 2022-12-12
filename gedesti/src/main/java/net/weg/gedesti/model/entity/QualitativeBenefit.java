package net.weg.gedesti.model.entity;

import lombok.*;

import javax.persistence.*;

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

    @Column(nullable = false)
    private boolean interalControlsRequirements;

}
