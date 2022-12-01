package net.weg.gedesti.dto;

import lombok.Data;
import lombok.ToString;
import net.weg.gedesti.model.entity.Funcionario;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import java.util.List;

@Data
@ToString
public class PautaDTO {
    private Integer agendaCode;
    @NotNull @Positive
    private Integer sequentialNumber;
    @NotNull
    private Integer yearAgenda;
    @NotNull
    List<Funcionario> commission;
}
