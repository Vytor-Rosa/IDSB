package net.weg.gedesti.repository;

import net.weg.gedesti.model.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttachmentReposiory extends JpaRepository<Attachment, Long> {
}
