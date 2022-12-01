package net.weg.gedesti.dto;

import lombok.Data;
import net.weg.gedesti.model.entity.Worker;
import net.weg.gedesti.model.entity.Agenda;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Date;
import java.util.List;

@Data
public class ProposalDTO {
    private Integer proposalCode;

    @NotBlank
    private String proposalName;

    @NotBlank
    private String proposalStatus;

    @NotNull
    @Positive
    private Double payback;

    @NotNull
    private Date initialRunPeriod;

    @NotNull
    private Date finalExecutionPeriod;

    @NotBlank
    private String descriptiveProposal;

    @NotNull
    private Worker responsibleAnalyst;

    @NotNull
    private Agenda agendaCode;

    List<Worker> workers;
}