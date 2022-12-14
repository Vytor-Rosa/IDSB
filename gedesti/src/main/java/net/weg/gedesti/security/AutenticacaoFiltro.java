//package net.weg.gedesti.security;
//
//import lombok.AllArgsConstructor;
//import net.weg.gedesti.model.entity.Worker;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@AllArgsConstructor
//public class AutenticacaoFiltro extends OncePerRequestFilter {
//
//    private JpaService jpaService;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        String token = request.getHeader("Authorization");
//        if (token != null && token.startsWith("Bearer ")) {
//            token = token.substring(7);
//        } else {
//            token = null;
//        }
//        Boolean valido = jpaService.validarToken(token);
//        if (valido) {
//            UserJpa usuario = jpaService.getUsuario(token);
//            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
//                    new UsernamePasswordAuthenticationToken(usuario.getUsername(),
//                            null, usuario.getAuthorities());
//            SecurityContextHolder.getContext().setAuthentication(
//                    usernamePasswordAuthenticationToken
//            );
//        }else if(!request.getRequestURI().equals("/editora-livros-api/login") || !request.getRequestURI().equals("/editora-livros-api/usuarios")){
//            response.setStatus(401);
//        }
//        filterChain.doFilter(request, response);
//    }
//}
