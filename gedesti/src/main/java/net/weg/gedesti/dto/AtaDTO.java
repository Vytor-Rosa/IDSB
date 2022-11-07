package net.weg.gedesti.dto;

import lombok.Getter;
import lombok.Setter;
import net.weg.gedesti.model.entity.Pauta;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class AtaDTO {
    private Integer codigoAta;
    @NotBlank
    private String nomeAta;
    @NotBlank
    private String problemaAta;
    @NotNull
    private Pauta pauta;
}