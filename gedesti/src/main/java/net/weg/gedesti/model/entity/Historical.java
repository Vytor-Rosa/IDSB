package net.weg.gedesti.model.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Bean;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;

@Entity
@Table(name = "historical")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Historical {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Integer historicalCode;

    @OneToOne
    private Demand demand;

    @OneToOne(cascade = CascadeType.ALL)
    private Attachment historicalAttachment;

    @Bean
    public void setAnexo(MultipartFile historicalAttachment){
        try{
            this.historicalAttachment = new Attachment(
                    historicalAttachment.getOriginalFilename(),
                    historicalAttachment.getContentType(),
                    historicalAttachment.getBytes()
            );
        }catch (Exception exception){
            throw new RuntimeException(exception);
        }
    }
}
