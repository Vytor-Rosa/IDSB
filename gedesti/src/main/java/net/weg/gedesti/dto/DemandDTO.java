package net.weg.gedesti.dto;

import lombok.Data;
import net.weg.gedesti.model.entity.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
public class DemandDTO {
    private Integer demandCode;

    private String demandTitle;

    private String currentProblem;

    private String demandObjective;

    private String demandStatus;

    private Double score;

    private String executionPeriod;

    private Worker requesterRegistration;

    private RealBenefit realBenefit;

    private QualitativeBenefit qualitativeBenefit;

    private PotentialBenefit potentialBenefit;

    List<CostCenter> costCenter;

    private Classification classification;

//    private Proposal proposal;
}
