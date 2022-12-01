package net.weg.gedesti.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "funcionario")
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class Worker {
//    implements UserDetails


    @Id
    @Column(nullable = false, unique = true)
    private Integer workerCode;

    @Column(nullable = false)
    private String workerName;

    @Column(nullable = false, unique = true)
    private String corporateEmail;

    @Column(nullable = false)
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