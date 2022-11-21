package net.weg.gedesti.dto;

import lombok.Data;
import net.weg.gedesti.model.entity.Demanda;

import javax.validation.constraints.NotNull;

@Data
public class ChatDTO {
    private Integer codigoChat;
    @NotNull
    private Demanda demanda;


}
