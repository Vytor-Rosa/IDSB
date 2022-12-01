package net.weg.gedesti.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import net.weg.gedesti.model.entity.Pauta;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Data
public class AtaDTO {
    private Integer minuteCode;

    @NotBlank
    private String minuteName;

    @NotBlank
    private String minuteProblem;

    @NotNull
    private Pauta agenda;
}
