package net.weg.gedesti.dto;

import lombok.Getter;
import lombok.Setter;
import net.weg.gedesti.model.entity.*;

import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter @Setter
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
    @NotNull
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
}
