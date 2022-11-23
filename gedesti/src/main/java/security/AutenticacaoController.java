//package security;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import javax.validation.Valid;
//
//@Controller
//@RequestMapping("/login")
//public class AutenticacaoController {
//
//    @Autowired
//    private AuthenticationManager authenticationManager;
//
//    @Autowired
//    private AutenticacaoService autenticacaoService;
//
//    @PostMapping
//    public ResponseEntity<Object> autenticacao(@RequestBody @Valid UsuarioDTO usuarioDTO){
//        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(usuarioDTO.getEmail(), usuarioDTO.getSenha());
//        Authentication authentication = authenticationManager.authenticate(authenticationToken);
//
//        if(authentication.isAuthenticated()){
//           String token =  autenticacaoService.gerarToken(authentication);
//            return ResponseEntity.status(HttpStatus.OK).body(new TokenDTO("Bearer", token));
//        }
//        return ResponseEntity.badRequest().build();
//    }
//}
