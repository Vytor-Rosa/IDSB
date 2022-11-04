package net.weg.gedesti.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class BeneficioQualitativoDTO {
    private Integer codigoBeneficioQualitativo;
    @NotBlank
    private String frequenciaDeUso;
    @NotNull
    private boolean requisitosControlesInternos;
}
