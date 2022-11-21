package net.weg.gedesti.dto;

import lombok.Data;
import net.weg.gedesti.model.entity.CentroDeCusto;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
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
