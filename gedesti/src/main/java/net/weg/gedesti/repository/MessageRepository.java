package net.weg.gedesti.repository;


import net.weg.gedesti.model.entity.Demand;
import net.weg.gedesti.model.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository <Message, Integer> {

    List<Message> findAllByDemand(Demand demand);

}
