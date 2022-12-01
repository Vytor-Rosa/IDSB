package net.weg.gedesti.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.weg.gedesti.dto.DemandDTO;
import net.weg.gedesti.model.entity.Demanda;

public class DemandaUtil {
    private ObjectMapper objectMapper = new ObjectMapper();

    public Demanda convertJsonToModel(String demandaJson) {
        DemandDTO demandaDTO = convetJsonToDTO(demandaJson);
        return convertDtoToModel(demandaDTO);
    }

    private DemandDTO convetJsonToDTO(String demandaJson) {
        try {
            return objectMapper.readValue(demandaJson, DemandDTO.class);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    private Demanda convertDtoToModel(DemandDTO demandaDTO) {
        return this.objectMapper.convertValue(demandaDTO, Demanda.class);
    }
}
