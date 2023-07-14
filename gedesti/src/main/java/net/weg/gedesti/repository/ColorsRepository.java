package net.weg.gedesti.repository;

import net.weg.gedesti.model.entity.Colors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ColorsRepository extends JpaRepository<Colors, Integer> {
}
