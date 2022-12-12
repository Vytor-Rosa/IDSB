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
    private String message;

    @Column(nullable = false)
    private Date dateMessage;

    @Column(nullable = false)
    private String messageTime;

    @ManyToOne
    private Chat chat;

    @OneToOne
    private Worker worker;


}
