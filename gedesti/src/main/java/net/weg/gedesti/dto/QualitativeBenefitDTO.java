package net.weg.gedesti.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class QualitativeBenefitDTO {
    private Integer qualitativeBenefitCode;

    @NotBlank
    private String frequencyOfUse;

    @NotNull
    private boolean interalControlsRequirements;
}
