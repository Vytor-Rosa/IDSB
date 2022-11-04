package net.weg.gedesti.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class BeneficioRealDTO {
    private Integer codigoBeneficioReal;
    @NotNull
    private Double valorMensalReal;
    @NotBlank
    private String descricaoBeneficioReal;
    @NotBlank
    private String moedaReal;
}
