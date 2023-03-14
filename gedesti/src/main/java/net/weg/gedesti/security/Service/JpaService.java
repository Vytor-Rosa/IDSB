//package net.weg.gedesti.security.Service;
//
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import lombok.AllArgsConstructor;
//import net.weg.gedesti.model.entity.Worker;
//import net.weg.gedesti.repository.WorkerRepository;
//import net.weg.gedesti.security.TokenUtils;
//import net.weg.gedesti.security.Users.UserJpa;
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
//@AllArgsConstructor
//public class JpaService implements UserDetailsService {
//
//    @Autowired
//    private WorkerRepository workerRepository;
//
//    @Override
//    public UserDetails loadUserByUsername(
//            String username) throws UsernameNotFoundException {
//        System.out.println(username);
//        Optional<Worker> workerOptional;
//
//        try {
//            Integer code = Integer.parseInt(username);
//            workerOptional = workerRepository.findById(code);
//        } catch (NumberFormatException e) {
//            workerOptional = workerRepository.findByCorporateEmail(username);
//        }
//        if (workerOptional.isPresent()) {
//            return new UserJpa(workerOptional.get());
//        }
//        throw new UsernameNotFoundException("Usuário não encontrado!");
//    }
//
//    public UserDetails loadUserByCode(Integer userCode) {
//        Optional<Worker> workerOptional =
//                workerRepository.findById(userCode);
//        if (workerOptional.isPresent()) {
//            return new UserJpa(workerOptional.get());
//        }
//        throw new UsernameNotFoundException("User not found!");
//    }
//}
