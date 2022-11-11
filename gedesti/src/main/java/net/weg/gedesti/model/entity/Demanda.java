package net.weg.gedesti.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Bean;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "demanda")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Demanda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Integer codigoDemanda;

    @Column(nullable = false)
    private String tituloDemanda;

    @Column(nullable = false)
    private String problemaAtual;

    @Column(nullable = false)
    private String objetivoDemanda;

    @Column(nullable = false)
    private String statusDemanda;

    @Column(nullable = false)
    private Double score;

    @Column(nullable = false)
    private String periodoDeExecucao;

    @ManyToOne
    private Funcionario matriculaSolicitante;

    @OneToOne
    private BeneficioReal beneficioReal;

    @OneToOne
    private BeneficioQualitativo beneficioQualitativo;

    @OneToOne
    private BeneficioPotencial beneficioPotencial;

    @OneToOne(cascade = CascadeType.ALL)
    private AnexoDemanda anexoDemanda;

    @Bean
    public void setAnexoDemanda(MultipartFile anexoDemanda){
        try{
            this.anexoDemanda = new AnexoDemanda(
                    anexoDemanda.getOriginalFilename(),
                    anexoDemanda.getContentType(),
                    anexoDemanda.getBytes()
            );
        }catch (Exception exception){
            throw new RuntimeException(exception);
        }
    }

    @ManyToMany
    @JoinTable(name = "centroDemanda",
            joinColumns = @JoinColumn(name = "codigoCentroDeCusto"),
            inverseJoinColumns = @JoinColumn(name = "codigoDemanda"))
    List<CentroDeCusto> centroDeCusto;
}
