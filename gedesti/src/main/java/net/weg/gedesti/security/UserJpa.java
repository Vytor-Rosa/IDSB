//package net.weg.gedesti.security;
//
//import net.weg.gedesti.model.entity.Worker;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.util.Collection;
//
//public class UserJpa implements UserDetails {
//    private Worker worker;
//
//    private Collection<GrantedAuthority> authorities;
//
//    private boolean accountNonExpired = true;
//    private boolean accountNonLocked = true;
//    private boolean credentialsNonExpired = true;
//    private boolean enabled = true;
//
//    public UserJpa(Worker worker){
//        this.worker = worker;
//    }
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return null;
//    }
//
//    @Override
//    public String getPassword() {
//        return worker.getWorkerPassword();
//    }
//
//    @Override
//    public String getUsername() {
//        return worker.getCorporateEmail();
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
//}
