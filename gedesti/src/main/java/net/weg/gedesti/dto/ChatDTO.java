package net.weg.gedesti.dto;

import lombok.Data;
import net.weg.gedesti.model.entity.Demand;

import javax.validation.constraints.NotNull;

@Data
public class ChatDTO {
    private Integer chatCode;
    @NotNull
    private Demand demand;


}
