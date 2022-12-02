package net.weg.gedesti.model.service;

import lombok.AllArgsConstructor;
import net.weg.gedesti.model.entity.Minute;
import net.weg.gedesti.repository.MinuteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MinuteService {
    private MinuteRepository ataRepository;

    public List<Minute> findAll() {
        return ataRepository.findAll();
    }

    public Page<Minute> findAll(Pageable pageable) {
        return ataRepository.findAll(pageable);
    }

    public <S extends Minute> S save(S entity) {
        return ataRepository.save(entity);
    }

    public boolean existsById(Integer integer) {
        return ataRepository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        ataRepository.deleteById(integer);
    }

    public Optional<Minute> findById(Integer integer) {
        return ataRepository.findById(integer);
    }
}
