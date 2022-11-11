package net.weg.gedesti.model.service;

import lombok.AllArgsConstructor;
import net.weg.gedesti.model.entity.Chat;
import net.weg.gedesti.repository.ChatRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ChatService {

    private ChatRepository chatRepository;

    public List<Chat> findAll() {
        return chatRepository.findAll();
    }

    public <S extends Chat> S save(S entity) {
        return chatRepository.save(entity);
    }

    public Optional<Chat> findById(Integer integer) {
        return chatRepository.findById(integer);
    }
}
