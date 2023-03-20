package net.weg.gedesti.dto;

import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CommissionDTO {

    @NotBlank Integer commissionCode;

    @NotBlank
    private String commissionName;

    @NotBlank
    private String commissionAcronym;

}
