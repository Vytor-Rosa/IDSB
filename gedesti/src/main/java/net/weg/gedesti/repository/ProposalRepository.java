package net.weg.gedesti.repository;

import net.weg.gedesti.model.entity.Demand;
import net.weg.gedesti.model.entity.Proposal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProposalRepository extends JpaRepository<Proposal, Integer> {
    Optional<Proposal> findByDemand(Demand demand);
    Optional<Proposal> findByDemandDemandCode(Integer demandCode);
}
