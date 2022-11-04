package net.weg.gedesti.repository;

import net.weg.gedesti.model.entity.CentroDemanda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CentroDemandaRepository extends JpaRepository<CentroDemanda, Integer> {
}
