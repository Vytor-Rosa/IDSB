package net.weg.gedesti.repository;

import net.weg.gedesti.model.entity.PotentialBenefit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BeneficioPotencialRepository extends JpaRepository<PotentialBenefit, Integer> {
}
