package net.weg.gedesti.dto;

import lombok.Data;
import net.weg.gedesti.model.entity.Attachment;
import net.weg.gedesti.model.entity.Demand;
import net.weg.gedesti.model.entity.Worker;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

@Data
public class HistoricalDTO {
    private Integer historicalCode;

    @NotNull
    private Demand demand;

    private Worker editor;

    private Demand newDemand;

    @NotNull
    private String historicalDate;

    @NotNull
    private String historicalHour;
}
