package net.weg.gedesti.model.entity;

import lombok.*;
import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.validator.constraints.Length;
import org.springframework.context.annotation.Bean;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
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

    private Date demandDate;

    private LocalTime demandHour;

    private String demandTitle;

    @Length(max = 5000)
    private String currentProblem;

    @Length(max = 5000)
    private String demandObjective;

    private String demandStatus;

    @Column(nullable = true)
    private Double score;
    
    @ManyToOne
    private Worker requesterRegistration;

    @ManyToOne
    private Worker approver;

    @OneToOne
    private RealBenefit realBenefit;

    @OneToOne
    private QualitativeBenefit qualitativeBenefit;

    @OneToOne
    private PotentialBenefit potentialBenefit;

    @OneToMany(cascade = CascadeType.ALL)
    private List<DemandAttachment> demandAttachments;

    @Bean
    public void setDemandAttachment(List<MultipartFile> demandAttachments) {
        this.demandAttachments = new ArrayList<>();

        for (MultipartFile file : demandAttachments) {
            try {
                DemandAttachment attachment = new DemandAttachment(
                        file.getOriginalFilename(),
                        file.getContentType(),
                        file.getBytes()
                );
                this.demandAttachments.add(attachment);
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
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
