package net.weg.gedesti.repository;

import net.weg.gedesti.model.entity.RealBenefit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RealBenefitRepository extends JpaRepository<RealBenefit, Integer> {
}
