package net.weg.gedesti.repository;

import net.weg.gedesti.model.entity.Agenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PautaRepository extends JpaRepository<Agenda, Integer> {
}
