package net.weg.gedesti.repository;

import net.weg.gedesti.model.entity.CostCenter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CentroDeCustoRepository extends JpaRepository<CostCenter, Integer> {

}
