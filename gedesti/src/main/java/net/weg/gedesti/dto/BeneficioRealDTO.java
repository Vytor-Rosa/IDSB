package net.weg.gedesti.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
public class BeneficioRealDTO {
    private Integer realBenefitCode;

    @NotNull @Positive
    private Double realMonthlyValue;

    @NotBlank
    private String realBenefitDescription;

    @NotBlank
    private String realCurrency;
}
