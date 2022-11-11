package net.weg.gedesti.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.weg.gedesti.dto.DemandaDTO;
import net.weg.gedesti.model.entity.Demanda;

public class DemandaUtil {
    private ObjectMapper objectMapper = new ObjectMapper();

    public Demanda convertJsonToModel(String demandaJson) {
        DemandaDTO demandaDTO = convetJsonToDTO(demandaJson);
        return convertDtoToModel(demandaDTO);
    }

    private DemandaDTO convetJsonToDTO(String demandaJson) {
        try {
            return objectMapper.readValue(demandaJson, DemandaDTO.class);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    private Demanda convertDtoToModel(DemandaDTO demandaDTO) {
        return this.objectMapper.convertValue(demandaDTO, Demanda.class);
    }
}
