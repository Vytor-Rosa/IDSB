package net.weg.gedesti.model.service;

import lombok.AllArgsConstructor;
import net.weg.gedesti.model.entity.Agenda;
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
    private MinuteRepository minuteRepository;

    public List<Minute> findAll() {
        return minuteRepository.findAll();
    }

    public Page<Minute> findAll(Pageable pageable) {
        return minuteRepository.findAll(pageable);
    }

    public <S extends Minute> S save(S entity) {
        return minuteRepository.save(entity);
    }

    public boolean existsById(Integer integer) {
        return minuteRepository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        minuteRepository.deleteById(integer);
    }

    public Optional<Minute> findById(Integer integer) {
        return minuteRepository.findById(integer);
    }

    public List<Minute> findByMinuteType(String minuteType) { return minuteRepository.findByMinuteType(minuteType); }

    public List<Minute> findByAgenda(Agenda agenda) { return minuteRepository.findByAgenda(agenda); }
}
