package net.weg.gedesti.repository;

import net.weg.gedesti.model.entity.Historico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoricoRepository extends JpaRepository<Historico, Integer> {

}