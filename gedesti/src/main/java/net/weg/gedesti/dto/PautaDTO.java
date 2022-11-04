package net.weg.gedesti.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.time.Year;

@Getter @Setter
public class PautaDTO {
    private Integer codigoPauta;
    private Integer numeroSequencial;
    private Year anoPauta;
}
