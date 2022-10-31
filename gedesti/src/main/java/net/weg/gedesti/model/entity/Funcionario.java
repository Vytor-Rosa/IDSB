package net.weg.gedesti.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "funcionario")
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class Funcionario {
    @Id
    @Column(nullable = false, unique = true)
    private Integer codigoFuncionario;

    @Column(nullable = false, unique = true)
    private String emailCorporativo;

    @Column(nullable = false)
    private String senhaFuncionario;

    @Column(nullable = false)
    private Integer cargoFuncionario;

}
