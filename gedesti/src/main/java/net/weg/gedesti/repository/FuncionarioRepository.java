package net.weg.gedesti.repository;

import net.weg.gedesti.model.entity.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FuncionarioRepository extends JpaRepository<Worker, Integer> {
    Worker findByWorkerPassword(String workerPassword);

    Optional<Worker> findByCorporateEmail(String corporateEmail);
}
