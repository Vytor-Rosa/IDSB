package net.weg.gedesti.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.weg.gedesti.model.entity.Bu;
import net.weg.gedesti.model.entity.Demanda;
import net.weg.gedesti.model.entity.Funcionario;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@Getter @Setter @ToString
public class ClassificacaoDTO {
    private Integer codigoClassificacao;

    @NotNull
    private Integer tamanhoClassificacao;

    @NotBlank
    private String secaoTI;

    @NotNull @Positive
    private Integer codigoPPM;

    @NotBlank
    private String linkEpicJira;

    @NotNull
    private Bu buSolicitante;

    @NotNull
    List<Bu> busBeneficiadas;

    @NotNull
    private Funcionario matriculaAnalista;
}
