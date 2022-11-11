package net.weg.gedesti.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Bean;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;

@Entity
@Table(name = "ata")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Ata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Integer codigoAta;

    @Column(nullable = false)
    private String nomeAta;

    @Column(nullable = false)
    private String problemaAta;

    @OneToOne(cascade = CascadeType.ALL)
    private Anexo anexo;

    @OneToOne
    private Pauta pauta;

    @Bean
    public void setAnexo(MultipartFile anexo){
        try{
            this.anexo = new Anexo(
                    anexo.getOriginalFilename(),
                    anexo.getContentType(),
                    anexo.getBytes()
            );
        }catch (Exception exception){
            throw new RuntimeException(exception);
        }
    }

}
