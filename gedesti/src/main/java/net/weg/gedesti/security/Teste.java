//package net.weg.gedesti.security;
//
//import net.weg.gedesti.model.entity.Worker;
//import org.springframework.net.weg.gedesti.security.core.userdetails.UserDetails;
//import org.springframework.net.weg.gedesti.security.core.userdetails.UserDetailsService;
//import org.springframework.net.weg.gedesti.security.core.userdetails.UsernameNotFoundException;
//
//public class Teste implements UserDetailsService {
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Funcionario funcionario = new Funcionario();
//        funcionario.setSenhaFuncionario("123");
//        funcionario.setEmailCorporativo("teste@teste");
//        return funcionario;
//    }
//}