package net.weg.gedesti.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Bean;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.sql.Time;
import java.util.Date;

@Entity
@Table(name = "mensagem")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Mensagem {

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
    private Funcionario worker;


}
