package net.weg.gedesti.model.service;

import lombok.AllArgsConstructor;
import net.weg.gedesti.model.entity.Colors;
import net.weg.gedesti.model.entity.Commission;
import net.weg.gedesti.model.entity.CostCenter;
import net.weg.gedesti.repository.ColorsRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ColorsService {


    private ColorsRepository colorsRepository;

    public Colors save(Colors entity) {
        return colorsRepository.save(entity);
    }


    public Optional<Colors> findById(Integer integer) {
        return colorsRepository.findById(integer);
    }


}
