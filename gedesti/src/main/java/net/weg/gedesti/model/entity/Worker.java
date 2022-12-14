package net.weg.gedesti.model.entity;

import lombok.*;
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

//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return List.of(new SimpleGrantedAuthority(cargoFuncionario));
//    }
//
//    @Override
//    public String getPassword() {
//        return senhaFuncionario;
//    }
//
//    @Override
//    public String getUsername() {
//        return emailCorporativo;
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
}
