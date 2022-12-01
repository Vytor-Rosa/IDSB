package net.weg.gedesti.dto;

import lombok.Data;
import net.weg.gedesti.model.entity.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class DemandaDTO {
    private Integer demandCode;

    @NotBlank
    private String demandTitle;

    @NotBlank
    private String currentProblem;

    @NotBlank
    private String demandObjective;

    @NotBlank
    private String demandStatus;

    @NotNull
    private Double score;

    @NotBlank
    private String executionPeriod;

    @NotNull
    private Funcionario requesterRegistration;

    @NotNull
    private BeneficioReal realBenefit;

    @NotNull
    private BeneficioQualitativo qualitativeBenefit;

    @NotNull
    private BeneficioPotencial potentialBenefit;

    @NotNull
    List<CentroDeCusto> costCenter;

    private Classificacao classification;

    private Proposta proposal;
}
