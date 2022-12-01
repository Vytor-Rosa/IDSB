package net.weg.gedesti.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "BU")
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class Bu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Integer buCode;

    @Column(nullable = false)
    private String bu;
}
