package net.weg.gedesti.dto;

import lombok.Getter;
import lombok.Setter;
import net.weg.gedesti.model.entity.Anexo;
import net.weg.gedesti.model.entity.Pauta;

@Getter @Setter
public class AtaDTO {

    private Integer codigoAta;
    private String nomeAta;
    private String problemaAta;
    private Anexo anexo;
    private Pauta pauta;
}
