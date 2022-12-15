//package net.weg.gedesti.security;
//
//
//import org.hibernate.annotations.common.reflection.XMethod;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@Configuration
//@EnableWebSecurity
//public class AutenticacaoConfig {
//    @Autowired
//    private JpaService jpaService;
//
//    @Bean
//    protected SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception {
//        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//        provider.setUserDetailsService(jpaService);
//        provider.setPasswordEncoder(new BCryptPasswordEncoder());
//        httpSecurity.authenticationProvider(provider);
//
//        httpSecurity.authorizeRequests()
//                .antMatchers("/gedesti/login", "/api/worker/{workerOffice}").permitAll()
//                .anyRequest().authenticated()
//                .and().csrf().disable()
//                .formLogin().permitAll()
//                .loginPage("/gedesti/login")
////                .defaultSuccessUrl().permitAll()
//                .and()
//                .logout().permitAll()
//                .logoutUrl("/gedesti/logout")
//                .logoutSuccessUrl("/gedesti/login").permitAll();
//        return httpSecurity.build();
//    }
//}