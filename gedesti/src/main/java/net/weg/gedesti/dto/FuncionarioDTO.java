package net.weg.gedesti.dto;

import lombok.Data;
import javax.validation.constraints.*;

@Data
public class FuncionarioDTO {
    @NotNull @Positive
    private Integer codigoFuncionario;
    @NotBlank
    private String nomeFuncionario;
    @Email
    private String emailCorporativo;
    @NotBlank
    private String senhaFuncionario;
    @PositiveOrZero
    private Integer cargoFuncionario;
}
