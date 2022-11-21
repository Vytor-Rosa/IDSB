package net.weg.gedesti.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CentroDeCustoDTO {
    @NotBlank
    private String centroDeCusto;

}
