package net.weg.gedesti.repository;

import net.weg.gedesti.model.entity.Proposal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProposalRepository extends JpaRepository<Proposal, Integer> {
    public Optional<Proposal> findByDemand(Integer integer);
}
