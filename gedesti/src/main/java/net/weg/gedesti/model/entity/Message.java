package net.weg.gedesti.model.entity;

import lombok.*;
import org.springframework.context.annotation.Bean;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "message")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Integer messageCode;

    private Boolean viewed;

    @Column(nullable = false)
    private String dateMessage;

    private Integer demandCode;

    @OneToOne
    private Worker sender;

    @Column(nullable = false)
    private String message;

    @OneToOne(cascade = CascadeType.ALL)
    private Attachment attachment;

    @Bean
    public void setDemandAttachment(MultipartFile attachment) {
        try {
            this.attachment = new Attachment(
                    attachment.getOriginalFilename(),
                    attachment.getContentType(),
                    attachment.getBytes()
            );
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

}
