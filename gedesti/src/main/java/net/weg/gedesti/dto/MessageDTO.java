package net.weg.gedesti.dto;

import lombok.Data;
import net.weg.gedesti.model.entity.Demand;
import net.weg.gedesti.model.entity.Worker;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class MessageDTO {
    private Integer messageCode;
    @NotBlank
    private String message;
    @NotNull
    private Date dateMessage;
    @NotBlank
    private String messageTime;
    @NotNull
    private Demand demand;
    @NotNull
    private Worker sender;
}
