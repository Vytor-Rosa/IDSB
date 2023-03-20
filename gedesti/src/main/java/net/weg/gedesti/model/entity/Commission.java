package net.weg.gedesti.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "commission")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Commission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer commissionCode;

    @Column(nullable = false)
    private String commissionName;

    @Column(nullable = false)
    private String commissionAcronym;

}
