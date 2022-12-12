package net.weg.gedesti.model.service;


import lombok.AllArgsConstructor;
import net.weg.gedesti.model.entity.Message;
import net.weg.gedesti.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MessageService {
    private MessageRepository messageRepository;

    public List<Message> findAll() {
        return messageRepository.findAll();
    }

    public <S extends Message> S save(S entity) {
        return messageRepository.save(entity);
    }

    public Optional<Message> findById(Integer integer) {
        return messageRepository.findById(integer);
    }

    public boolean existsById(Integer integer) {
        return messageRepository.existsById(integer);
    }
}
