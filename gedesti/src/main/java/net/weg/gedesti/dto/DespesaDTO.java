package net.weg.gedesti.dto;

import lombok.Getter;
import lombok.Setter;
import net.weg.gedesti.model.entity.CentroDeCusto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter @Setter
public class DespesaDTO {
    private Integer codigoDespesa;
    @NotBlank
    private String tipoDespesa;
    @NotBlank
    private String perfilDespesa;
    @NotNull @Positive
    private Integer periodoExecucao;
    @NotBlank
    private String quantidadeHoras;
    @NotNull
    private Double valorHora;
    @NotNull
    private Double valorTotal;
    @NotNull
    private CentroDeCusto centroDeCusto;
//    @NotNull
//    private Proposta proposta;
}
