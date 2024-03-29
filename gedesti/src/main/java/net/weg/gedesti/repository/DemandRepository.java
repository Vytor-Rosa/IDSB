package net.weg.gedesti.repository;

import net.weg.gedesti.model.entity.Demand;
import net.weg.gedesti.model.entity.Worker;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DemandRepository extends JpaRepository<Demand, Integer> {
    List<Demand> findByDemandStatus(String status);
    @Query("SELECT d FROM Demand d WHERE d.activeVersion = true")
    List<Demand> findAllByActiveVersion();
    @Query("SELECT d FROM Demand d WHERE d.activeVersion = true ORDER BY d.score DESC")
    Page<Demand> findAllByActiveVersionOrderByScoreDesc(Pageable pageable);
    @Query("SELECT d FROM Demand d WHERE d.activeVersion = true ORDER BY d.score DESC")
    List<Demand> findAllByActiveVersionOrderByScoreDesc();
    List<Demand> findAllByRequesterRegistration(Worker worker);
    List<Demand> findByDemandCode(Integer integer);
    List<Demand> findAllByDemandCode(Integer integer);
    Demand findByDemandCodeAndDemandVersion(Integer demandCode, Integer demandVersion);
    List<Demand> findAllByApprover(Worker worker);
    List<Demand> findAllByClassificationAnalistRegistry(Worker worker);
    List<Demand> findAllByDemandVersion(Integer integer);
    @Query("SELECT d FROM Demand d WHERE d.activeVersion = true AND d.demandStatus = 'BacklogRanked' ORDER BY d.score DESC")
    Page<Demand> findAllByActiveVersionAndDemandStatusOrderByScoreDesc(Pageable pageable);
    @Query("SELECT d FROM Demand d WHERE d.activeVersion = true AND d.requesterRegistration.department = :department ORDER BY d.score DESC")
    Page<Demand> findAllByActiveVersionAndDepartmentOrderByScoreDesc(@Param("department") String department, Pageable pageable);
}
