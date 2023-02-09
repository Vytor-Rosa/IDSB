package net.weg.gedesti.model.service;

import lombok.AllArgsConstructor;
import net.weg.gedesti.model.entity.Notification;
import net.weg.gedesti.model.entity.Proposal;
import net.weg.gedesti.repository.NotificationRepository;
import net.weg.gedesti.repository.ProposalRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class NotificationService {

    private NotificationRepository notificationRepository;

    public List<Notification> findAll() {
        return notificationRepository.findAll();
    }

    public <S extends Notification> S save(S entity) {
        return notificationRepository.save(entity);
    }

    public Optional<Notification> findById(Integer integer) {
        return notificationRepository.findById(integer);
    }

    public boolean existsById(Integer integer) {
        return notificationRepository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        notificationRepository.deleteById(integer);
    }
}
