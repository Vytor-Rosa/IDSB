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
//public class JpaService implements UserDetailsService {
//
//    @Autowired
//    private WorkerRepository workerRepository;
//
//    private final String senhaForte = "c127a7b6adb013a5ff879ae71afa62afa4b4ceb72afaa54711dbcde67b6dc325";
//
//    @Override
//    public UserDetails loadUserByUsername(
//            String username) throws UsernameNotFoundException {
//        System.out.println(username);
//        Optional<Worker> workerOptional =
//                workerRepository.findByCorporateEmail(username);
//        if (workerOptional.isPresent()) {
//            return new UserJpa(workerOptional.get());
//        }
//        throw new UsernameNotFoundException("Usuário não encontrado!");
//    }
//
//    public String gerarToken(Authentication authentication) {
//        Worker worker = (Worker) authentication.getPrincipal();
//        return Jwts.builder()
//                .setIssuer("Gedesti")
//                .setSubject(worker.getWorkerCode().toString())
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(new Date().getTime() + 1800000))
//                .signWith(SignatureAlgorithm.HS256, senhaForte)
//                .compact();
//    }
//
//    public Boolean validarToken(String token) {
//        try {
//            Jwts.parser().setSigningKey(senhaForte).parseClaimsJws(token);
//            return true;
//        } catch (Exception e) {
//            return false;
//        }
//
//    }
//
//    public UserJpa getUsuario(String token) {
//        Integer workerCode = Integer.parseInt(Jwts.parser()
//                .setSigningKey(senhaForte)
//                .parseClaimsJws(token)
//                .getBody().getSubject());
//        return new UserJpa(workerRepository.findById(workerCode).get());
//    }
//}
