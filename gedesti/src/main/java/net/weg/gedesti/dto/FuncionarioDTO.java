package net.weg.gedesti.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.*;

@Getter @Setter
public class FuncionarioDTO {
    @NotNull @Positive
    private Integer codigoFuncionario;
    @Email
    private String emailCorporativo;
    @NotBlank
    private String senhaFuncionario;
    @PositiveOrZero
    private Integer cargoFuncionario;
}
