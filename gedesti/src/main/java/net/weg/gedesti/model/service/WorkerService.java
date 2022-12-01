package net.weg.gedesti.model.service;

import lombok.AllArgsConstructor;
import net.weg.gedesti.model.entity.Worker;
import net.weg.gedesti.repository.FuncionarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class WorkerService {
    private FuncionarioRepository funcionarioRepository;

    public List<Worker> findAll() {
        return funcionarioRepository.findAll();
    }

    public <S extends Worker> S save(S entity) {
        return funcionarioRepository.save(entity);
    }

    public Optional<Worker> findById(Integer integer) {
        return funcionarioRepository.findById(integer);
    }

    public boolean existsById(Integer integer) {
        return funcionarioRepository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        funcionarioRepository.deleteById(integer);
    }

    public Optional<Worker> findByCorporateEmail(String corporateEmail) {
        return funcionarioRepository.findByCorporateEmail(corporateEmail);
    }

    public Worker findByWorkerPassword(String workerPassword){
        return funcionarioRepository.findByWorkerPassword(workerPassword);
    }

}
