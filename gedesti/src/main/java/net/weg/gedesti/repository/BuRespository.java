package net.weg.gedesti.repository;

import net.weg.gedesti.model.entity.Bu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.Entity;

@Repository
public interface BuRespository extends JpaRepository<Bu, Integer> {
}
