package net.weg.gedesti.repository;

import net.weg.gedesti.model.entity.Comissao;
import net.weg.gedesti.model.entity.Funcionario;
import net.weg.gedesti.model.entity.Pauta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComissaoRepository extends JpaRepository<Comissao, Integer> {
//    List<Object> findByPauta(Pauta pauta);
}
