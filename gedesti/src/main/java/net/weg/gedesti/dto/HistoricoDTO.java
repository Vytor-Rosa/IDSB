package net.weg.gedesti.dto;

import lombok.Getter;
import lombok.Setter;
import net.weg.gedesti.model.entity.Anexo;
import net.weg.gedesti.model.entity.Demanda;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class HistoricoDTO {
    private Integer codigoHistorico;

    @NotNull
    private Demanda demanda;

//    @NotNull
//    private Anexo anexo;
}
