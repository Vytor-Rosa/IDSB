package net.weg.gedesti.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

@Getter @Setter
public class BeneficioQualitativoDTO {
    private Integer codigoBeneficioQualitativo;
    private String frequeciaDeUso;
    private boolean requisitosControlesInternos;
}
