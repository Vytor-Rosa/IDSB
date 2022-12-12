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
    @Column(nullable = false, unique = true)
    private Integer classificationCode;

    @Column(nullable = false)
    private Integer classificationSize;

    @Column(nullable = false)
    private String ITSection;

    @Column(nullable = false)
    private Integer PPMCode;

    @Column(nullable = false)
    private String EpicJiraLink;

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
