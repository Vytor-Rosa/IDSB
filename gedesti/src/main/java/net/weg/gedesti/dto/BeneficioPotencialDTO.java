package net.weg.gedesti.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class BeneficioPotencialDTO {
    private Integer codigoBeneficioPotencial;
    @NotNull
    private Double valorMensalPotencial;
    @NotNull
    private Boolean obrigacaoLegal;
    @NotBlank
    private String moedaPotencial;
}
