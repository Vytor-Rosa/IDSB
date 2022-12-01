package net.weg.gedesti.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.weg.gedesti.dto.MinuteDTO;
import net.weg.gedesti.model.entity.Minute;

public class AtaUtil {
    private ObjectMapper objectMapper = new ObjectMapper();

    public Minute convertJsonToModel(String ataJson){
        MinuteDTO ataDTO = convetJsonToDTO(ataJson);
        return convertDtoToModel(ataDTO);
    }

    private MinuteDTO convetJsonToDTO(String ataJson){
        try{
            return objectMapper.readValue(ataJson, MinuteDTO.class);
        }catch (Exception exception){
            throw new RuntimeException(exception);
        }
    }

    private Minute convertDtoToModel(MinuteDTO ataDTO){
        return this.objectMapper.convertValue(ataDTO, Minute.class);
    }


}
