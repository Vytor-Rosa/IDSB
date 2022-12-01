package net.weg.gedesti.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.weg.gedesti.dto.HistoricalDTO;
import net.weg.gedesti.model.entity.Historico;
import org.springframework.beans.BeanUtils;

public class HistoricoUtil {

    private ObjectMapper objectMapper = new ObjectMapper();

    public Historico convertJsonToModel(String historicoJson) {
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

    private Historico convertDtoToModel(HistoricalDTO historicoDTO) {
        Historico historico = new Historico();
        BeanUtils.copyProperties(historicoDTO, historico);
        return historico;
    }
}
