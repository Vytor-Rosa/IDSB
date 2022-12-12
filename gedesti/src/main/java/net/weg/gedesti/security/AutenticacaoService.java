//package net.weg.gedesti.security;
//
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import net.weg.gedesti.model.entity.Worker;
//import net.weg.gedesti.repository.WorkerRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.Date;
//import java.util.Optional;
//
//@Service
//public class AutenticacaoService
//        implements UserDetailsService
//{
//
//    @Autowired
//    private WorkerRepository workerRepository;
//
//    private final String senhaForte = "c127a7b6adb013a5ff879ae71afa62afa4b4ceb72afaa54711dbcde67b6dc325";
//
//    @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        Optional<Worker> funcionarioOptional = workerRepository.findByCorporateEmail(email);
//        if(funcionarioOptional.isPresent()){
//            return funcionarioOptional.get();
//        }
//        throw new UsernameNotFoundException("Erro! Usuario não encontrado");
//    }
//
//    public String gerarToken(Authentication authentication) {
//        Worker worker = (Worker) authentication.getPrincipal();
//        return Jwts.builder().setIssuer("Gedesti")
//                .setSubject(worker.getWorkerCode().toString())
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(new Date().getTime() + 1800000))
//                .signWith(SignatureAlgorithm.HS256, senhaForte)
//                .compact();
//    }
//
//    public Boolean validarToken(String token){
//        try{
//            Jwts.parser().setSigningKey(senhaForte).parseClaimsJws(token);
//            return true;
//        }catch (Exception e){
//            return false;
//        }
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
//    public Worker getFuncionario(String token){
//        Integer matricula = Integer.parseInt(Jwts.parser()
//                .setSigningKey(senhaForte)
//                .parseClaimsJws(token)
//                .getBody().getSubject());
//        return workerRepository.findById(matricula).get();
//    }
//
//}
