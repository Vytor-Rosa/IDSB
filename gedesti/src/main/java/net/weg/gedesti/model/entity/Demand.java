package net.weg.gedesti.model.entity;

import lombok.*;
import net.minidev.json.annotate.JsonIgnore;
import org.springframework.context.annotation.Bean;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "demand")
@NoArgsConstructor
@AllArgsConstructor
@Data
@IdClass(DemandId.class)
public class Demand {
    @Id
    @Column(nullable = false)
    private Integer demandCode;

    @Id
    private Integer demandVersion;

    private Boolean activeVersion;

    private String demandDate;

    private LocalTime demandHour;

    private String demandTitle;

    @Size(max = 5000)
    private String currentProblem;

    @Size(max = 5000)
    private String demandObjective;

    private String demandStatus;

    @Column(nullable = true)
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
    @JoinColumns({
            @JoinColumn(name = "demandCode", referencedColumnName = "demandCode"),
            @JoinColumn(name = "demandVersion", referencedColumnName = "demandVersion")
    })
    List<CostCenter> costCenter;

    @JoinColumn(nullable = true)
    @OneToOne
    private Classification classification;

}
