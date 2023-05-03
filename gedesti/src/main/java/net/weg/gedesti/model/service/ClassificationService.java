package net.weg.gedesti.model.service;

import lombok.AllArgsConstructor;
import net.weg.gedesti.model.entity.Classification;
import net.weg.gedesti.repository.ClassificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ClassificationService {

    ClassificationRepository classificationRepository;

    public List<Classification> findAll() {
        return classificationRepository.findAll();
    }

    public <S extends Classification> S save(S entity) {
        return classificationRepository.save(entity);
    }

    public Optional<Classification> findById(Integer integer) {
        return classificationRepository.findById(integer);
    }



    public boolean existsById(Integer integer) {
        return classificationRepository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        classificationRepository.deleteById(integer);
    }

    public Object saveAndFlush(Classification classification) {
        return classificationRepository.saveAndFlush(classification);
    }
}
