package net.weg.gedesti.dto;

import lombok.Data;
import net.weg.gedesti.model.entity.Attachment;
import net.weg.gedesti.model.entity.Demand;
import javax.validation.constraints.NotNull;

@Data
public class HistoricalDTO {
    private Integer historicalCode;

    @NotNull
    private Demand demand;

    @NotNull
    private Attachment historicalAttachment;
}
