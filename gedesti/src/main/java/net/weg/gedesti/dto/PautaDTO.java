package net.weg.gedesti.dto;

import lombok.Getter;
import lombok.Setter;
import net.weg.gedesti.model.entity.Comissao;
import net.weg.gedesti.model.entity.Funcionario;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Year;
import java.util.List;

@Getter
@Setter
public class PautaDTO {
    private Integer codigoPauta;

    @NotNull
    private Integer numeroSequencial;

    @NotNull
    private Year anoPauta;

    @NotNull
    private List<Comissao> funcionarios;
}