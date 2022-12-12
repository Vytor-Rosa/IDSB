package net.weg.gedesti.model.service;

import lombok.AllArgsConstructor;
import net.weg.gedesti.model.entity.Worker;
import net.weg.gedesti.repository.WorkerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class WorkerService {
    private WorkerRepository workerRepository;

    public List<Worker> findAll() {
        return workerRepository.findAll();
    }

    public <S extends Worker> S save(S entity) {
        return workerRepository.save(entity);
    }

    public Optional<Worker> findById(Integer integer) {
        return workerRepository.findById(integer);
    }

    public boolean existsById(Integer integer) {
        return workerRepository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        workerRepository.deleteById(integer);
    }

    public Optional<Worker> findByCorporateEmail(String corporateEmail) {
        return workerRepository.findByCorporateEmail(corporateEmail);
    }

    public Worker findByWorkerPassword(String workerPassword) {
        return workerRepository.findByWorkerPassword(workerPassword);
    }

}
