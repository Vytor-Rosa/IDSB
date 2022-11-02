package net.weg.gedesti.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

@Getter @Setter
public class BeneficioPotencialDTO {
    private Integer codigoBeneficioPotencial;
    private Double valorMensalPotencial;
    private Boolean obrigacaoLegal;
    private String moedaPotencial;
}
