//package security;
//
//import org.apache.catalina.filters.HttpHeaderSecurityFilter;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//@Configuration
//@EnableWebSecurity
//public class AutenticacaoConfig extends WebSecurityConfigurerAdapter {
//
//    @Autowired
//    private AutenticacaoService autenticacaoService;
//
//    protected void configure(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity.authorizeRequests()
//                .antMatchers(HttpMethod.POST,"/api/funcionarios/").permitAll()
//                .anyRequest().authenticated()
//                .and().csrf().disable();
////                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
////                .and().addFilterBefore(new AutenticacaoFiltro(autenticacaoService), UsernamePasswordAuthenticationFilter.class);
//    }
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
//        authenticationManagerBuilder.userDetailsService(autenticacaoService)
//                .passwordEncoder(new BCryptPasswordEncoder());
//    }
//
//    @Bean
//    @Override
//    protected AuthenticationManager authenticationManager() throws Exception {
//        return super.authenticationManager();
//    }
//
//}