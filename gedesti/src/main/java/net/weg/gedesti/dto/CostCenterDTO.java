package net.weg.gedesti.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CostCenterDTO {
    @NotBlank
    private String costCenter;

}
