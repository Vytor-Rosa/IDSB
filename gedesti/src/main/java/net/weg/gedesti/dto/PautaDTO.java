package net.weg.gedesti.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Year;

@Getter @Setter
public class PautaDTO {
    private Integer codigoPauta;
    @NotNull
    private Integer numeroSequencial;
    @NotNull
    private Year anoPauta;
}
