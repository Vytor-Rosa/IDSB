package net.weg.gedesti.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.weg.gedesti.dto.DemandDTO;
import net.weg.gedesti.model.entity.Demand;

public class DemandUtil {
    private ObjectMapper objectMapper = new ObjectMapper();

    public Demand convertJsonToModel(String demandJson) {
        DemandDTO demandDTO = convetJsonToDTO(demandJson);
        return convertDtoToModel(demandDTO);
    }

    private DemandDTO convetJsonToDTO(String demandJson) {
        try {
            return objectMapper.readValue(demandJson, DemandDTO.class);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    private Demand convertDtoToModel(DemandDTO demandDTO) {
        return this.objectMapper.convertValue(demandDTO, Demand.class);
    }
}
