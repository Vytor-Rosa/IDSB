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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class DemandaDTO {
    private Integer codigoDemanda;
    @NotBlank
    private String tituloDemanda;
    @NotBlank
    private String problemaAtual;
    @NotBlank
    private String objetivoDemanda;
    @NotBlank
    private String statusDemanda;
    @NotBlank
    private String solicitante;
    @NotNull
    private Funcionario matriculaFuncionario;
    @NotNull
    private BeneficioReal beneficioReal;
    @NotNull
    private BeneficioQualitativo beneficioQualitativo;
    @NotNull
    private BeneficioPotencial beneficioPotencial;
}
