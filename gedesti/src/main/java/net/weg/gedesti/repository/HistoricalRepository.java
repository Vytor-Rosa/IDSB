package net.weg.gedesti.repository;

import net.weg.gedesti.model.entity.Historical;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoricalRepository extends JpaRepository<Historical, Integer> {

}
