package net.weg.gedesti.repository;

import net.weg.gedesti.model.entity.Comissao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComissaoRepository extends JpaRepository<Comissao, Integer> {
}
