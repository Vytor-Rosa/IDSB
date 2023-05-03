package net.weg.gedesti.model.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "message")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Integer messageCode;


    @Column(nullable = false)
    private String dateMessage;

    private Integer demandCode;

    @OneToOne
    private Worker sender;

    @Column(nullable = false)
    private String message;
}
