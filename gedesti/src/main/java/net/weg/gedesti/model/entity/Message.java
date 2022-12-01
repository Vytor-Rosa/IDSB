package net.weg.gedesti.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "mensagem")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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