package net.weg.gedesti.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Getter @Setter
public class FuncionarioDTO {
    @NotNull
    private Integer codigoFuncionario;
    @Email
    private String emailCorporativo;
    @NotBlank
    private String senhaFuncionario;
    @PositiveOrZero
    private Integer cargoFuncionario;
}
