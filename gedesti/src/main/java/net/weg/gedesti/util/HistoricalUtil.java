package net.weg.gedesti.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.weg.gedesti.dto.HistoricalDTO;
import net.weg.gedesti.model.entity.Historical;
import org.springframework.beans.BeanUtils;

public class HistoricalUtil {

    private ObjectMapper objectMapper = new ObjectMapper();

    public Historical convertJsonToModel(String historicalJson) {
        HistoricalDTO historicalDTO = convetJsonToDTO(historicalJson);
        return convertDtoToModel(historicalDTO);
    }

    private HistoricalDTO convetJsonToDTO(String historicalJson) {
        try {
            return objectMapper.readValue(historicalJson, HistoricalDTO.class);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    private Historical convertDtoToModel(HistoricalDTO historicalDTO) {
        Historical historical = new Historical();
        BeanUtils.copyProperties(historicalDTO, historical);
        return historical;
    }
}
