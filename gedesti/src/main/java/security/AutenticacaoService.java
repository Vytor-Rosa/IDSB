package security;

import net.weg.gedesti.model.entity.Funcionario;
import net.weg.gedesti.repository.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AutenticacaoService implements UserDetailsService {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    private final String senhaForte = "c127a7b6adb013a5ff879ae71afa62afa4b4ceb72afaa54711dbcde67b6dc325";

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Funcionario> funcionarioOptional = funcionarioRepository.findByEmailCorporativo(email);
        if(funcionarioOptional.isPresent()){
            return funcionarioOptional.get();
        }
        throw new UsernameNotFoundException("Erro! Usuario não encontrado");
    }
}
