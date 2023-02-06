package net.weg.gedesti.repository;

import net.weg.gedesti.model.entity.Reproach;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;

@Repository
public interface ReproachRepositry extends JpaRepository<Reproach, Integer> {
}
