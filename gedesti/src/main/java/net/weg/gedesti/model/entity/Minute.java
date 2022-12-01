package net.weg.gedesti.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Bean;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;

@Entity
@Table(name = "minute")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Minute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Integer minuteCode;

    @Column(nullable = false)
    private String minuteName;

    @Column(nullable = false)
    private String minuteProblem;

    @OneToOne(cascade = CascadeType.ALL)
    private Attachment attachment;

    @OneToOne
    private Agenda agenda;

    @Bean
    public void setAttachment(MultipartFile attachment){
        try{
            this.attachment = new Attachment(
                    attachment.getOriginalFilename(),
                    attachment.getContentType(),
                    attachment.getBytes()
            );
        }catch (Exception exception){
            throw new RuntimeException(exception);
        }
    }

}
