package net.weg.gedesti.model.service;

import lombok.AllArgsConstructor;
import net.weg.gedesti.model.entity.Bu;
import net.weg.gedesti.repository.BuRespository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BuService {
    private BuRespository buRespository;

    public List<Bu> findAll() {
        return buRespository.findAll();
    }

    public <S extends net.weg.gedesti.model.entity.Bu> S save(S entity) {
        return buRespository.save(entity);
    }

    public Optional<Bu> findById(Integer integer) {
        return buRespository.findById(integer);
    }

    public boolean existsById(Integer integer) {
        return buRespository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        buRespository.deleteById(integer);
    }
}
