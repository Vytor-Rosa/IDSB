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
    private Integer codigoHistorico;

    @OneToOne
    private Demanda demanda;

    @OneToOne(cascade = CascadeType.ALL)
    private Anexo anexoHistorico;

    @Bean
    public void setAnexo(MultipartFile anexoHistorico){
        try{
            this.anexoHistorico = new Anexo(
                    anexoHistorico.getOriginalFilename(),
                    anexoHistorico.getContentType(),
                    anexoHistorico.getBytes()
            );
        }catch (Exception exception){
            throw new RuntimeException(exception);
        }
    }
}
