package net.weg.gedesti.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "colors")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Colors {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer colorsCode;

    @Column(nullable = false)
    private String color1;

    @Column(nullable = false)
    private String color2;

    @Column(nullable = false)
    private String color3;

    @Column(nullable = false)
    private String color4;

    @Column(nullable = false)
    private String color5;

    @Column(nullable = false)
    private String color6;

    @Column(nullable = false)
    private String color7;

    @Column(nullable = false)
    private String color8;

    @Column(nullable = false)
    private String color9;
}
