package net.weg.gedesti.model.service;

import lombok.AllArgsConstructor;
import net.weg.gedesti.model.entity.Agenda;
import net.weg.gedesti.model.entity.Proposal;
import net.weg.gedesti.repository.AgendaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AgendaService {
    private AgendaRepository agendaRepository;

    public List<Agenda> findAll() {
        return agendaRepository.findAll();
    }

    public Page<Agenda> findAll(Pageable pageable) {
        return agendaRepository.findAll(pageable);
    }

    public <S extends Agenda> S save(S entity) {
        return agendaRepository.save(entity);
    }

    public Optional<Agenda> findById(Integer integer) {
        return agendaRepository.findById(integer);
    }

    public boolean existsById(Integer integer) {
        return agendaRepository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        agendaRepository.deleteById(integer);
    }

}
