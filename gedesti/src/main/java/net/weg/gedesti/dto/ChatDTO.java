package net.weg.gedesti.dto;

import lombok.Getter;
import lombok.Setter;
import net.weg.gedesti.model.entity.Demanda;

import javax.validation.constraints.NotNull;

@Getter @Setter
public class ChatDTO {
    private Integer codigoChat;
    @NotNull
    private Demanda demanda;


}
