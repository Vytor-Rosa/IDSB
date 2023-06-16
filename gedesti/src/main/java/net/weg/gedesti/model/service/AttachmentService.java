package net.weg.gedesti.model.service;

import lombok.AllArgsConstructor;
import net.weg.gedesti.model.entity.Attachment;
import net.weg.gedesti.repository.AttachmentReposiory;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AttachmentService {
    private AttachmentReposiory attachmentReposiory;

    public <S extends Attachment> S save(S entity) {
        return attachmentReposiory.save(entity);
    }


}
