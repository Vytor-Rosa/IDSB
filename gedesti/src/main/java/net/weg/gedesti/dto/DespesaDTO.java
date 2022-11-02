package net.weg.gedesti.dto;

import lombok.Getter;
import lombok.Setter;
import net.weg.gedesti.model.entity.CentroDeCusto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter @Setter
public class DespesaDTO {
    private Integer codigoDespesa;
    private String tipoDespesa;
    private String perfilDespesa;
    private Integer periodoExecucao;
    private String quantidadeHoras;
    private Double valorHora;
    private Double valorTotal;
    private CentroDeCusto centroDeCusto;
}
