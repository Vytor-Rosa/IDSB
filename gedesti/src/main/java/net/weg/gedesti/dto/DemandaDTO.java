package net.weg.gedesti.dto;

import lombok.Data;
import net.weg.gedesti.model.entity.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class DemandaDTO {
    private Integer codigoDemanda;

    @NotBlank
    private String tituloDemanda;

    @NotBlank
    private String problemaAtual;

    @NotBlank
    private String objetivoDemanda;

    @NotBlank
    private String statusDemanda;

    @NotNull
    private Double score;

    @NotBlank
    private String periodoDeExecucao;

    @NotNull
    private Funcionario matriculaSolicitante;

    @NotNull
    private BeneficioReal beneficioReal;

    @NotNull
    private BeneficioQualitativo beneficioQualitativo;

    @NotNull
    private BeneficioPotencial beneficioPotencial;

    @NotNull
    List<CentroDeCusto> centroDeCusto;


    private Classificacao classificacao;

    private Proposta proposta;
}
