package net.weg.gedesti.repository;

import net.weg.gedesti.model.entity.CostCenter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CostCenterRepository extends JpaRepository<CostCenter, Integer> {

    Optional<CostCenter> findByCostCenter(String costCenter);
}
