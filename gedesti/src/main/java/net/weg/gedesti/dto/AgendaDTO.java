package net.weg.gedesti.dto;

import lombok.Data;
import lombok.ToString;
import net.weg.gedesti.model.entity.Commission;
import net.weg.gedesti.model.entity.Proposal;
import net.weg.gedesti.model.entity.Worker;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import java.util.List;

@Data
@ToString
public class AgendaDTO {
    private Integer agendaCode;
    @NotNull
    @Positive
    private Integer sequentialNumber;
    @NotNull
    private String initialDate;
    @NotNull
    private String finalDate;
    @NotNull
    private Commission commission;
    @NotNull
    private String agendaDate;
    @NotNull
    List<Proposal> proposals;

    private Worker analistRegistry;
}
