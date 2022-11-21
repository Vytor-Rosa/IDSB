package net.weg.gedesti.dto;

import lombok.Data;
import net.weg.gedesti.model.entity.Anexo;
import net.weg.gedesti.model.entity.Demanda;
import javax.validation.constraints.NotNull;

@Data
public class HistoricoDTO {
    private Integer codigoHistorico;

    @NotNull
    private Demanda demanda;

    @NotNull
    private Anexo anexoHistorico;
}
