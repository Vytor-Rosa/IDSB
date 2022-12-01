package net.weg.gedesti.dto;

import lombok.Data;
import net.weg.gedesti.model.entity.CentroDeCusto;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
public class DespesaDTO {
    private Integer expenseCode;
    @NotBlank
    private String expenseType;
    @NotBlank
    private String expenseProfile;
    @NotNull @Positive
    private Integer runTime;
    @NotBlank
    private String amountOfHours;
    @NotNull
    private Double hourValue;
    @NotNull
    private Double totalValue;
    @NotNull
    private CentroDeCusto costCenter;
//    @NotNull
//    private Proposta proposta;
}
