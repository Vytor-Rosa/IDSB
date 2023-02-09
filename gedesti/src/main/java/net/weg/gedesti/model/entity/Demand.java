package net.weg.gedesti.model.entity;

import lombok.*;
import org.springframework.context.annotation.Bean;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "demand")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Demand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Integer demandCode;
    private String demandDate;
    private String demandTitle;
    private String currentProblem;
    private String demandObjective;
    private String demandStatus;
    private Double score;
    private String executionPeriod;

    @ManyToOne
    private Worker requesterRegistration;

    @OneToOne
    private RealBenefit realBenefit;

    @OneToOne
    private QualitativeBenefit qualitativeBenefit;

    @OneToOne
    private PotentialBenefit potentialBenefit;

    @OneToOne(cascade = CascadeType.ALL)
    private DemandAttachment demandAttachment;

    @Bean
    public void setDemandAttachment(MultipartFile demandAttachment) {
        try {
            this.demandAttachment = new DemandAttachment(
                    demandAttachment.getOriginalFilename(),
                    demandAttachment.getContentType(),
                    demandAttachment.getBytes()
            );
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @ManyToMany
    @JoinTable(name = "demandCenter",
            joinColumns = @JoinColumn(name = "costCenterCode"),
            inverseJoinColumns = @JoinColumn(name = "demandCode"))
    List<CostCenter> costCenter;

    @JoinColumn(nullable = true)
    @OneToOne
    private Classification classification;

//    @JoinColumn(nullable = true)
//    @OneToOne
//    private Proposal proposal;
}
