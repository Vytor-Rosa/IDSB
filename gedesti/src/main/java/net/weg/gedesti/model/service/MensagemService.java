package net.weg.gedesti.model.service;


import lombok.AllArgsConstructor;
import net.weg.gedesti.model.entity.Mensagem;
import net.weg.gedesti.repository.MensagemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MensagemService {
    private MensagemRepository mensagemRepository;

    public List<Mensagem> findAll() {
        return mensagemRepository.findAll();
    }

    public <S extends Mensagem> S save(S entity) {
        return mensagemRepository.save(entity);
    }

    public Optional<Mensagem> findById(Integer integer) {
        return mensagemRepository.findById(integer);
    }

    public boolean existsById(Integer integer) {
        return mensagemRepository.existsById(integer);
    }
}
