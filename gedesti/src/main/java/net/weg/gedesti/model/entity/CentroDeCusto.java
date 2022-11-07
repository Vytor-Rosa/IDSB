package net.weg.gedesti.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "centroDeCusto")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CentroDeCusto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Integer codigoCentroDeCusto;

    @Column(nullable = false)
    private String centroDeCusto;
}
