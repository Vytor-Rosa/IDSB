package net.weg.gedesti.model.entity;


import lombok.*;
import org.springframework.context.annotation.Bean;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.ArrayList;
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

    @OneToMany(cascade = CascadeType.ALL)
    private List<Attachment> classificationAttachment;

    @Bean
    public void setAttachment(List<MultipartFile> classificationAttachment) {
        this.classificationAttachment = new ArrayList<>();

        for(MultipartFile file : classificationAttachment) {
            try {
                 Attachment attachment = new Attachment(
                         file.getOriginalFilename(),
                         file.getContentType(),
                         file.getBytes()
                );
                 this.classificationAttachment.add(attachment);
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        }
    }

    @ManyToMany
    @JoinTable(name = "beneficiariesBus",
            joinColumns = @JoinColumn(name = "buCode"),
            inverseJoinColumns = @JoinColumn(name = "classificationCode"))
    private List<Bu> beneficiaryBu;

    @ManyToOne
    private Worker analistRegistry;
}
