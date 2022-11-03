package net.weg.gedesti.dto;

import lombok.Getter;
import lombok.Setter;
import net.weg.gedesti.model.entity.BeneficioPotencial;
import net.weg.gedesti.model.entity.BeneficioQualitativo;
import net.weg.gedesti.model.entity.BeneficioReal;
import net.weg.gedesti.model.entity.Funcionario;

import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Getter @Setter
public class DemandaDTO {
    private Integer codigoDemanda;
    private String tituloDemanda;
    private String problemaAtual;
    private String objetivoDemanda;
    private String statusDemanda;
    private String solicitante;
    private Funcionario matriculaFuncionario;
    private BeneficioReal beneficioReal;
    private BeneficioQualitativo beneficioQualitativo;
    private BeneficioPotencial beneficioPotencial;
}
