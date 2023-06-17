package net.weg.gedesti.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import net.weg.gedesti.model.entity.Attachment;
import net.weg.gedesti.model.entity.Demand;
import net.weg.gedesti.model.entity.Worker;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class MessageDTO {
    private String message;
    @NotNull
    private String dateMessage;
    @NotNull
    private Integer demandCode;
    @NotNull
    private Worker sender;
    private Attachment attachment;
}
