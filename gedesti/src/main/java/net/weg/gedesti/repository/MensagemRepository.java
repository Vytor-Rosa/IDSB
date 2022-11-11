package net.weg.gedesti.repository;


import net.weg.gedesti.model.entity.Mensagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MensagemRepository extends JpaRepository <Mensagem, Integer> {
}
