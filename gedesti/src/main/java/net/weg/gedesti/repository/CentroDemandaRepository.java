package net.weg.gedesti.repository;

import net.weg.gedesti.model.entity.CentroDeCusto;
import net.weg.gedesti.model.entity.CentroDemanda;
import net.weg.gedesti.model.entity.Demanda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CentroDemandaRepository extends JpaRepository<CentroDemanda, Integer> {
    List<Object> findByDemanda(Demanda demanda);
}
