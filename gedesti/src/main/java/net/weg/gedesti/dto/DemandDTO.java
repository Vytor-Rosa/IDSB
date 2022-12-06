package net.weg.gedesti.dto;

import lombok.Data;
import net.weg.gedesti.model.entity.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class DemandDTO {
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
    private Worker requesterRegistration;

    @NotNull
    private RealBenefit realBenefit;

    @NotNull
    private QualitativeBenefit qualitativeBenefit;

    @NotNull
    private PotentialBenefit potentialBenefit;

    @NotNull
    List<CostCenter> costCenter;

    private Classification classification;

    private Proposal proposal;
}
