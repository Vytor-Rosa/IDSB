package net.weg.gedesti.dto;

import lombok.Data;
import javax.validation.constraints.*;

@Data
public class FuncionarioDTO {
    @NotNull @Positive
    private Integer workerCode;
    @NotBlank
    private String workerName;
    @Email
    private String corporateEmail;
    @NotBlank
    private String workerPassword;
    @PositiveOrZero
    private Integer workerOffice;
}
