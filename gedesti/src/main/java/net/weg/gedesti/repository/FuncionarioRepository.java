package net.weg.gedesti.repository;

import net.weg.gedesti.model.entity.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario, Integer> {
    Funcionario findBySenhaFuncionario(String senhaFuncionario);

    Optional<Funcionario> findByEmailCorporativo(String emailCorporativo);
}
