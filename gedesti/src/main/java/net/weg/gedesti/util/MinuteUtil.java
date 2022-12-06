package net.weg.gedesti.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.weg.gedesti.dto.MinuteDTO;
import net.weg.gedesti.model.entity.Minute;

public class MinuteUtil {
    private ObjectMapper objectMapper = new ObjectMapper();

    public Minute convertJsonToModel(String minuteJson) {
        MinuteDTO minuteDTO = convetJsonToDTO(minuteJson);
        return convertDtoToModel(minuteDTO);
    }

    private MinuteDTO convetJsonToDTO(String minuteJson) {
        try {
            return objectMapper.readValue(minuteJson, MinuteDTO.class);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    private Minute convertDtoToModel(MinuteDTO minuteDTO) {
        return this.objectMapper.convertValue(minuteDTO, Minute.class);
    }
}