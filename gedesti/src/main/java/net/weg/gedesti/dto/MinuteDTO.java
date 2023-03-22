package net.weg.gedesti.dto;

import lombok.Data;
import net.weg.gedesti.model.entity.Agenda;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class MinuteDTO {
    private Integer minuteCode;

    @NotBlank
    private String minuteName;

    private String minuteStartDate;

    private String minuteEndDate;

    @NotNull
    private Agenda agenda;
}
