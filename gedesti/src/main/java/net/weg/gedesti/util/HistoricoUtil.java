package net.weg.gedesti.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.weg.gedesti.dto.HistoricoDTO;
import net.weg.gedesti.model.entity.Historico;

public class HistoricoUtil {

    private ObjectMapper objectMapper = new ObjectMapper();

    public Historico convertJsonToModel(String historicoJson) {
        HistoricoDTO historicoDTO = convetJsonToDTO(historicoJson);
        return convertDtoToModel(historicoDTO);
    }

    private HistoricoDTO convetJsonToDTO(String historicoJson) {
        try {
            return objectMapper.readValue(historicoJson, HistoricoDTO.class);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    private Historico convertDtoToModel(HistoricoDTO historicoDTO) {
        return this.objectMapper.convertValue(historicoDTO, Historico.class);
    }
}
