package net.weg.gedesti.repository;

import net.weg.gedesti.model.entity.Demand;
import net.weg.gedesti.model.entity.Proposal;
import net.weg.gedesti.model.entity.Worker;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProposalRepository extends JpaRepository<Proposal, Integer> {
    Optional<Proposal> findByDemand(Demand demand);
    Optional<Proposal> findByDemandDemandCode(Integer demandCode);
    @Query("SELECT p FROM Proposal p ORDER BY p.score DESC")
    List<Proposal> findAllOrderByScoreDesc();
    @Query("SELECT p FROM Proposal p ORDER BY p.score DESC")
    Page<Proposal> findAllOrderByScoreDesc(Pageable pageable);
    List<Proposal> findAllByResponsibleAnalyst(Worker workerCode);
}
