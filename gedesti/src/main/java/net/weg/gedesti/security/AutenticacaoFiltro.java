package net.weg.gedesti.security;

import lombok.AllArgsConstructor;
import net.weg.gedesti.security.Service.JpaService;
import net.weg.gedesti.security.Users.UserJpa;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@AllArgsConstructor
public class AutenticacaoFiltro extends OncePerRequestFilter {

    private JpaService jpaService;
    private TokenUtils tokenUtils = new TokenUtils();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(request.getRequestURI().equals("/login/auth") || request.getRequestURI().equals("/login") || request.getRequestURI().equals("/logout") || request.getRequestURI().equals("/login/logout") || request.getRequestURI().contains("/v3/api-docs/") || request.getRequestURI().contains("/swagger-ui/*")){
            filterChain.doFilter(request, response);
            return;
        }

        String token = tokenUtils.buscarCookie(request);

        Boolean valido = tokenUtils.validarToken(token);
        if (valido) {
            Integer userCode = tokenUtils.getUsuarioCode(token);
            UserDetails user = jpaService.loadUserByCode(userCode);


            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(user.getUsername(),null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(
                    usernamePasswordAuthenticationToken
            );
            filterChain.doFilter(request, response);
            return;
        }
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
