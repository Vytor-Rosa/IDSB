package net.weg.gedesti.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.weg.gedesti.dto.HistoricalDTO;
import net.weg.gedesti.model.entity.Historical;
import org.springframework.beans.BeanUtils;

public class HistoricalUtil {

    private ObjectMapper objectMapper = new ObjectMapper();

    public Historical convertJsonToModel(String historicoJson) {
        HistoricalDTO historicoDTO = convetJsonToDTO(historicoJson);
        return convertDtoToModel(historicoDTO);
    }

    private HistoricalDTO convetJsonToDTO(String historicoJson) {
        try {
            return objectMapper.readValue(historicoJson, HistoricalDTO.class);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    private Historical convertDtoToModel(HistoricalDTO historicoDTO) {
        Historical historico = new Historical();
        BeanUtils.copyProperties(historicoDTO, historico);
        return historico;
    }
}
