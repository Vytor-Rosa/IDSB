package net.weg.gedesti.model.entity;


import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "classification")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Classification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = true, unique = true)
    private Integer classificationCode;

    @Column(nullable = true)
    private String classificationSize;

    @Column(nullable = true)
    private String itSection;

    @Column(nullable = true)
    private String ppmCode;

    @Column(nullable = true)
    private String epicJiraLink;
    
    @OneToOne
    private Bu requesterBu;

    @ManyToMany
    @JoinTable(name = "beneficiariesBus",
            joinColumns = @JoinColumn(name = "buCode"),
            inverseJoinColumns = @JoinColumn(name = "classificationCode"))
    private List<Bu> beneficiaryBu;

    @ManyToOne
    private Worker analistRegistry;
}
