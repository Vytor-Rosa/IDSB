package net.weg.gedesti.repository;

import net.weg.gedesti.model.entity.Minute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AtaRepository extends JpaRepository <Minute, Integer> {
}
