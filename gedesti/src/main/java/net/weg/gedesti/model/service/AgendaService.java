package net.weg.gedesti.model.service;

import lombok.AllArgsConstructor;
import net.weg.gedesti.model.entity.Agenda;
import net.weg.gedesti.repository.PautaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AgendaService {
    private PautaRepository pautaRepository;

    public List<Agenda> findAll() {
        return pautaRepository.findAll();
    }

    public Page<Agenda> findAll(Pageable pageable) {
        return pautaRepository.findAll(pageable);
    }

    public <S extends Agenda> S save(S entity) {
        return pautaRepository.save(entity);
    }

    public Optional<Agenda> findById(Integer integer) {
        return pautaRepository.findById(integer);
    }

    public boolean existsById(Integer integer) {
        return pautaRepository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        pautaRepository.deleteById(integer);
    }
}
