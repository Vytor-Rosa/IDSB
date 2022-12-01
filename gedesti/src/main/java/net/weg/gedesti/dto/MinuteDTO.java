package net.weg.gedesti.dto;

import lombok.Data;
import net.weg.gedesti.model.entity.Agenda;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class MinuteDTO {
    private Integer minuteCode;

    @NotBlank
    private String minuteName;

    @NotBlank
    private String minuteProblem;

    @NotNull
    private Agenda agenda;
}
