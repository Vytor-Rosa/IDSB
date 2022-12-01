package net.weg.gedesti.model.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Bean;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;

@Entity
@Table(name = "Historico")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Historico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Integer historicalCode;

    @OneToOne
    private Demanda demand;

    @OneToOne(cascade = CascadeType.ALL)
    private Anexo historicalAttached;

    @Bean
    public void setAnexo(MultipartFile anexoHistorico){
        try{
            this.historicalAttached = new Anexo(
                    anexoHistorico.getOriginalFilename(),
                    anexoHistorico.getContentType(),
                    anexoHistorico.getBytes()
            );
        }catch (Exception exception){
            throw new RuntimeException(exception);
        }
    }
}
