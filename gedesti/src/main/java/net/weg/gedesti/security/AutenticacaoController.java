package net.weg.gedesti.security;

import net.weg.gedesti.model.entity.Worker;
import net.weg.gedesti.security.Service.JpaService;
import net.weg.gedesti.security.Users.UserJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@RequestMapping("/login")
public class AutenticacaoController {

    private TokenUtils tokenUtils = new TokenUtils();

//    @Autowired
//    private JpaService jpaService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/auth")
    public ResponseEntity<Object> autenticacao(
            @RequestBody @Valid UsuarioDTO usuarioDTO, HttpServletResponse response) {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        usuarioDTO.getCorporateEmail(), usuarioDTO.getWorkerPassword());

        Authentication authentication =
                authenticationManager.authenticate(authenticationToken);
        if (authentication.isAuthenticated()) {
            UserJpa userJpa = (UserJpa) authentication.getPrincipal();
            Worker worker = userJpa.getWorker();
            response.addCookie(tokenUtils.gerarCookie(authentication));

            Cookie userCookie = tokenUtils.gerarUserCookie(userJpa);
            response.addCookie(userCookie);

            return ResponseEntity.status(HttpStatus.OK).body(worker);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
