package net.weg.gedesti.dto;

import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CommissionDTO {

    @NotBlank
    private String comissionName;

    @NotBlank
    private String comissionAcronym;

}
