package net.weg.gedesti.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.weg.gedesti.dto.ClassificationDTO;
import net.weg.gedesti.model.entity.Classification;

public class ClassificationUtil {
    private ObjectMapper objectMapper = new ObjectMapper();

    public Classification convertJsonToModel(String classificationJson) {
        ClassificationDTO classificationDTO = convetJsonToDTO(classificationJson);
        return convertDtoToModel(classificationDTO);
    }

    private ClassificationDTO convetJsonToDTO(String classificationJson) {
        try {
            return objectMapper.readValue(classificationJson, ClassificationDTO.class);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    private Classification convertDtoToModel(ClassificationDTO classificationDTO) {
        return this.objectMapper.convertValue(classificationDTO, Classification.class);
    }
}
