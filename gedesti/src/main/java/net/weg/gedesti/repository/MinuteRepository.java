package net.weg.gedesti.repository;

import net.weg.gedesti.model.entity.Agenda;
import net.weg.gedesti.model.entity.Minute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MinuteRepository extends JpaRepository <Minute, Integer> {
    List<Minute> findByMinuteType(String minuteType);
    List<Minute> findByAgenda(Agenda agenda);
}
