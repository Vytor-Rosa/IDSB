package net.weg.gedesti.repository;

import net.weg.gedesti.model.entity.Proposal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropostaRepository extends JpaRepository<Proposal, Integer> {
}
