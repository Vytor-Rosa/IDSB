package net.weg.gedesti.dto;

import lombok.Getter;
import lombok.Setter;
import net.weg.gedesti.model.entity.Demanda;
import net.weg.gedesti.model.entity.Funcionario;
import net.weg.gedesti.model.entity.Pauta;

import java.util.Date;

@Getter @Setter
public class PropostaDTO {
    private Integer codigoProposta;
    private String nomeProposta;
    private String statusProposta;
    private Double payback;
    private Date periodoExecucaoInicial;
    private Date periodoExecucaoFinal;
    private String descritivoProposta;
    private Demanda codigoDemanda;
    private Funcionario analistaResponsavel;
    private Pauta codigoPauta;
}
