package net.weg.gedesti.repository;

import net.weg.gedesti.model.entity.Ata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AtaRepository extends JpaRepository <Ata, Integer> {
}
