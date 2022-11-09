package net.weg.gedesti.model.service;

import lombok.AllArgsConstructor;
import net.weg.gedesti.model.entity.Ata;
import net.weg.gedesti.repository.AtaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AtaService {
    private AtaRepository ataRepository;

    public List<Ata> findAll() {
        return ataRepository.findAll();
    }

    public Page<Ata> findAll(Pageable pageable) {
        return ataRepository.findAll(pageable);
    }

    public <S extends Ata> S save(S entity) {
        return ataRepository.save(entity);
    }

    public boolean existsById(Integer integer) {
        return ataRepository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        ataRepository.deleteById(integer);
    }

    public Optional<Ata> findById(Integer integer) {
        return ataRepository.findById(integer);
    }
}
