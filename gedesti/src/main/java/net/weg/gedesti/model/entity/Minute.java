package net.weg.gedesti.model.entity;

import lombok.*;
import org.springframework.context.annotation.Bean;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "minute")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Minute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Integer minuteCode;

    @Column(nullable = false)
    private String minuteName;

    @Column(nullable = true)
    private String minuteStartDate;

    private String minuteEndDate;

    @OneToOne
    private Agenda agenda;

    @OneToOne
    private Worker director;

}
