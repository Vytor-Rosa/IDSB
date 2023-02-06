package net.weg.gedesti.model.service;

import lombok.AllArgsConstructor;
import net.weg.gedesti.model.entity.Reproach;
import net.weg.gedesti.repository.RealBenefitRepository;
import net.weg.gedesti.repository.ReproachRepositry;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ReproachService {
    private ReproachRepositry reproachRepositry;

    public List<Reproach> findAll() {
        return reproachRepositry.findAll();
    }

    public <S extends Reproach> S save(S entity) {
        return reproachRepositry.save(entity);
    }

    public Optional<Reproach> findById(Integer integer) {
        return reproachRepositry.findById(integer);
    }
}
