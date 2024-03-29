package net.weg.gedesti.model.entity;

import lombok.*;
import org.springframework.context.annotation.Bean;
import org.springframework.web.multipart.MultipartFile;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.net.weg.gedesti.security.core.GrantedAuthority;
//import org.springframework.net.weg.gedesti.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.net.weg.gedesti.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "worker")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Worker {
//    implements UserDetails


    @Id
    @Column(nullable = true, unique = true)
    private Integer workerCode;

    @Column(nullable = true)
    private String workerName;

    @Column(nullable = true, unique = true)
    private String corporateEmail;

    @Column(nullable = true)
    private String workerPassword;

    @Column(nullable = false)
    private String workerOffice;

    @Column(nullable = false)
    private String language;

    @Column(nullable = false)
    private String department;

    @Column(nullable = false)
    private Boolean voiceCommand;

    @Column(nullable = false)
    private Boolean pounds;

    @Column(nullable = false)
    private Boolean screenReader;

    @Column(nullable = false)
    private Boolean darkmode;

    @Column(nullable = false)
    private Boolean square;

    @Column(nullable = false)
    private Integer fontSize;

    @OneToOne
    private Colors colors;

    @OneToOne(cascade = CascadeType.ALL)
    private Attachment workerPhoto;

    @Bean
    public void setAttachment(MultipartFile workerPhoto) {
        try {
            this.workerPhoto = new Attachment(
                    workerPhoto.getOriginalFilename(),
                    workerPhoto.getContentType(),
                    workerPhoto.getBytes()
            );
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

}
