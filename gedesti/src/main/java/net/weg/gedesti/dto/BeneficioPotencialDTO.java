package net.weg.gedesti.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter @Setter
public class BeneficioPotencialDTO {
    private Integer codigoBeneficioPotencial;

    @NotNull @Positive
    private Double valorMensalPotencial;

    @NotNull
    private Boolean obrigacaoLegal;

    @NotBlank
    private String moedaPotencial;
}
