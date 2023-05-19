package net.weg.gedesti.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
public class PotentialBenefitDTO {
    private Integer potentialBenefitCode;

    @NotNull @Positive
    private Double potentialMonthlyValue;

    private Boolean legalObrigation;

    @NotBlank
    private String potentialBenefitDescription;

    @NotBlank
    private String potentialCurrency;
}
