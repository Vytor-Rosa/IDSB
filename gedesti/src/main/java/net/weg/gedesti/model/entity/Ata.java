package net.weg.gedesti.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "ata")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Ata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Integer codigoAta;

    @Column(nullable = false)
    private String nomeAta;

    @Column(nullable = false)
    private String problemaAta;

    @OneToOne
    private Anexo anexo;

    @OneToOne
    private Pauta pauta;

}
