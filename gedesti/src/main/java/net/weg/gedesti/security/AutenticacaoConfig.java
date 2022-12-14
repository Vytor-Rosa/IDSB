//package net.weg.gedesti.security;
//
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
//    // Configura as autorizações de acesso
//
//    @Bean
//    protected SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception {
//        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//        provider.setUserDetailsService(jpaService);
//        provider.setPasswordEncoder(new BCryptPasswordEncoder());
//        httpSecurity.authenticationProvider(provider);
//
//        httpSecurity.authorizeRequests()
//                // Libera o acesso sem autenticação para /login
//                .antMatchers("/api/worker/logins", "api/worker").permitAll()
//                // Determina que todas as demais requisições terão de ser autenticadas
//                .anyRequest().authenticated()
//                .and().csrf().disable()
//                .formLogin().permitAll()
//                .loginPage("/api/worker/logins")
//                .defaultSuccessUrl("/gedesti/home")
//                .and()
//                .logout().permitAll()
//                .logoutUrl("/api/worker/logout")
//                .logoutSuccessUrl("/api/worker/logins").permitAll();
////                .and()
////
////                .sessionManagement().sessionCreationPolicy(
////                SessionCreationPolicy.STATELESS)
////                .and().addFilterBefore(
////                new AutenticacaoFiltro(jpaService),
////                UsernamePasswordAuthenticationFilter.class);
//        return httpSecurity.build();
//    }
//
//
//    // Configura a autenticação para os acessos
//
//    protected void configure(
//            AuthenticationManagerBuilder authenticationManagerBuilder)
//            throws Exception {
//        authenticationManagerBuilder
//                .userDetailsService(jpaService)
//                .passwordEncoder(new BCryptPasswordEncoder());
//    }
//
//}