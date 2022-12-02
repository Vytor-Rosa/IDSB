package net.weg.gedesti.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.weg.gedesti.dto.DemandDTO;
import net.weg.gedesti.model.entity.Demand;

public class DemandUtil {
    private ObjectMapper objectMapper = new ObjectMapper();

    public Demand convertJsonToModel(String demandaJson) {
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

    private Demand convertDtoToModel(DemandDTO demandaDTO) {
        return this.objectMapper.convertValue(demandaDTO, Demand.class);
    }
}
