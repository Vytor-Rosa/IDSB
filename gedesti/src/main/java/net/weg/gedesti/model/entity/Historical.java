package net.weg.gedesti.model.entity;


import lombok.*;
import org.springframework.context.annotation.Bean;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;

@Entity
@Table(name = "historical")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Historical {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Integer historicalCode;

    @OneToOne
    private Demand demand;

    @Transient
    private Demand newDemand;

    @ManyToOne
    private Worker editor;

    @Column(nullable = false)
    private String historicalDate;

    @Column(nullable = false)
    private String historicalHour;
}
