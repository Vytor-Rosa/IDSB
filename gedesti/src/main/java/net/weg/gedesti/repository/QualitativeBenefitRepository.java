package net.weg.gedesti.repository;

import net.weg.gedesti.model.entity.QualitativeBenefit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QualitativeBenefitRepository extends JpaRepository<QualitativeBenefit, Integer> {

}
