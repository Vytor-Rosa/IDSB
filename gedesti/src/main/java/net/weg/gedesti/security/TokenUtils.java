//package net.weg.gedesti.security;
//
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import net.weg.gedesti.model.entity.Worker;
//import net.weg.gedesti.repository.WorkerRepository;
//import net.weg.gedesti.security.Users.UserJpa;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.web.util.WebUtils;
//
//import javax.servlet.http.Cookie;
//import javax.servlet.http.HttpServletRequest;
//import java.util.Date;
//import java.util.Optional;
//
//public class TokenUtils {
//
//    private final String senhaForte = "c127a7b6adb013a5ff879ae71afa62afa4b4ceb72afaa54711dbcde67b6dc325";
//
//    public String gerarToken(Authentication authentication) {
//        UserJpa userJpa = (UserJpa) authentication.getPrincipal();
//        Worker worker = userJpa.getWorker();
//        return Jwts.builder()
//                .setIssuer("ids")
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
//    public Integer getUsuarioCode(String token) {
//        return Integer.parseInt(Jwts.parser()
//                .setSigningKey(senhaForte)
//                .parseClaimsJws(token)
//                .getBody().getSubject());
//    }
//
//    public String buscarCookie(HttpServletRequest request) {
//        Cookie cookie = WebUtils.getCookie(request, "jwt");
//        System.out.println("cookie: " + cookie);
//        if(cookie != null){
//            return cookie.getValue();
//        }
//        throw new RuntimeException("Cookie não encontrado!");
//    }
//}
