package net.weg.gedesti.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "similarity")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Similarity {

    @Id
    @Column(nullable = true, unique = true)
    private Integer similarityCode;

    @OneToOne
    private Demand demandOne;

    @OneToOne
    private Demand demandTwo;

    @Column(nullable = true)
    private Double similarityValue;

}
