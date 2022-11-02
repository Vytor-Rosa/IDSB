package net.weg.gedesti.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

@Getter @Setter
public class BeneficioRealDTO {
    private Integer codigoBeneficioReal;
    private Double valorMensalReal;
    private String descricaoBeneficioReal;
    private String moedaReal;
}
