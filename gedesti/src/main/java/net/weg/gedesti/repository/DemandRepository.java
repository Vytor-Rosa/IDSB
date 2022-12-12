package net.weg.gedesti.repository;

import net.weg.gedesti.model.entity.Demand;
import net.weg.gedesti.model.entity.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DemandRepository extends JpaRepository<Demand, Integer> {
}
