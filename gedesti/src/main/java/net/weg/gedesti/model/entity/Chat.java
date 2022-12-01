package net.weg.gedesti.model.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "chat")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Integer chatCode;

    @OneToOne
    @JoinColumn(nullable = false)
    private Demanda demand;


}
