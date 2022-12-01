package net.weg.gedesti.repository;

import net.weg.gedesti.model.entity.Classification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassificacaoRepository extends JpaRepository<Classification, Integer> {
}
