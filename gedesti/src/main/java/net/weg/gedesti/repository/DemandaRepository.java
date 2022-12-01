package net.weg.gedesti.repository;

import net.weg.gedesti.model.entity.Demand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DemandaRepository extends JpaRepository<Demand, Integer> {
}
