package net.weg.gedesti.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

@Getter @Setter
public class FuncionarioDTO {
    private String emailCorporativo;
    private String senhaFuncionario;
}
