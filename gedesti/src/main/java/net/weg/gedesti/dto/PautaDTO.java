package net.weg.gedesti.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.weg.gedesti.model.entity.Funcionario;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import java.util.List;

@Getter
@Setter
@ToString
public class PautaDTO {
    private Integer codigoPauta;
    @NotNull @Positive
    private Integer numeroSequencial;
    @NotNull
    private Integer anoPauta;
    @NotNull
    List<Funcionario> comissao;
}
