package net.weg.gedesti.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.weg.gedesti.dto.AtaDTO;
import net.weg.gedesti.model.entity.Ata;

public class AtaUtil {
    private ObjectMapper objectMapper = new ObjectMapper();

    public Ata convertJsonToModel(String ataJson){
        AtaDTO ataDTO = convetJsonToDTO(ataJson);
        return convertDtoToModel(ataDTO);
    }

    private AtaDTO convetJsonToDTO(String ataJson){
        try{
            return objectMapper.readValue(ataJson, AtaDTO.class);
        }catch (Exception exception){
            throw new RuntimeException(exception);
        }
    }

    private Ata convertDtoToModel(AtaDTO ataDTO){
        return this.objectMapper.convertValue(ataDTO, Ata.class);
    }


}
