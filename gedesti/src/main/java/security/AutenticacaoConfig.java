//package security;
//
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//
//@Configuration
//@EnableWebSecurity
//public class AutenticacaoConfig extends WebSecurityConfigurerAdapter {
//
//    @Autowired
//    private Teste autenticacaoService;
//
//    @Override
//    protected void configure(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity.authorizeRequests()
//                .antMatchers("/login").permitAll()
//                .anyRequest().authenticated()
//
//                .and().csrf().disable();
//
////                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
////                .and().addFilterBefore(new AutenticacaoFiltro(autenticacaoService), UsernamePasswordAuthenticationFilter.class);
//    }
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
//
////        authenticationManagerBuilder.userDetailsService(autenticacaoService)
//        authenticationManagerBuilder.userDetailsService(autenticacaoService)
//                .passwordEncoder(new BCryptPasswordEncoder());
//    }
//
////    @Bean
////    protected UserDetailsService userDetailsService(){
////        UserDetails user = User.withDefaultPasswordEncoder()
////                .username("admin")
////                .password("admin")
////                .roles("ADM")
////                .build();
////        return new InMemoryUserDetailsManager(user);
////    }
//
////    @Bean
////    @Override
////    protected AuthenticationManager authenticationManager() throws Exception {
////        return super.authenticationManager();
////    }
//
//}