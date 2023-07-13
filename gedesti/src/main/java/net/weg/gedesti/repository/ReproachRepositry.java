package net.weg.gedesti.repository;

import net.weg.gedesti.model.entity.Demand;
import net.weg.gedesti.model.entity.Reproach;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.Optional;

@Repository
public interface ReproachRepositry extends JpaRepository<Reproach, Integer> {
    Optional<Reproach> findByDemand(Demand demand);

}
