package net.weg.gedesti.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "funcionario")
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class Funcionario {
//    implements UserDetails
    @Id
    @Column(nullable = false, unique = true)
    private Integer codigoFuncionario;

    @Column(nullable = false)
    private String nomeFuncionario;

    @Column(nullable = false, unique = true)
    private String emailCorporativo;

    @Column(nullable = false)
    private String senhaFuncionario;

    @Column(nullable = false)
    private Integer cargoFuncionario;

//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return null;
//    }
//
//    @Override
//    public String getPassword() {
//        return null;
//    }
//
//    @Override
//    public String getUsername() {
//        return null;
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return false;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return false;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return false;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return false;
//    }
}
