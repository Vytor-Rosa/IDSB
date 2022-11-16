package net.weg.gedesti.dto;

import lombok.Getter;
import lombok.Setter;
import net.weg.gedesti.model.entity.Demanda;
import net.weg.gedesti.model.entity.Funcionario;
import net.weg.gedesti.model.entity.Pauta;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class PropostaDTO {
    private Integer codigoProposta;

    @NotBlank
    private String nomeProposta;

    @NotBlank
    private String statusProposta;

    @NotNull
    @Positive
    private Double payback;

    @NotNull
    private Date periodoExecucaoInicial;

    @NotNull
    private Date periodoExecucaoFinal;

    @NotBlank
    private String descritivoProposta;

    @NotNull
    private Funcionario analistaResponsavel;

    @NotNull
    private Pauta codigoPauta;

    List<Funcionario> Funcionarios;
}
