package net.weg.gedesti.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class FuncionarioDTO {
    @NotNull
    private Integer codigoFuncionario;
    @Email
    private String emailCorporativo;
    @NotBlank
    private String senhaFuncionario;
    private Integer cargoFuncionario;
}
