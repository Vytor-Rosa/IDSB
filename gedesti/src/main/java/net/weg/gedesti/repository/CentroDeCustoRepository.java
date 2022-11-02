package net.weg.gedesti.repository;

import net.weg.gedesti.model.entity.CentroDeCusto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CentroDeCustoRepository extends JpaRepository<CentroDeCusto, Integer> {

}
