package net.weg.gedesti.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter @Setter
public class CentroDeCustoDTO {
    @NotBlank
    private String centroDeCusto;

}
