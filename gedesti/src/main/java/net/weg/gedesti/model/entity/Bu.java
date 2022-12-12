package net.weg.gedesti.model.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "BU")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Bu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Integer buCode;

    @Column(nullable = false)
    private String bu;
}
