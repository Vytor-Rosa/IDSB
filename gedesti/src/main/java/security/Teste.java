package security;

import net.weg.gedesti.model.entity.Funcionario;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class Teste implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Funcionario funcionario = new Funcionario();
        funcionario.setSenhaFuncionario("123");
        funcionario.setEmailCorporativo("teste@teste");
        return funcionario;
    }
}
