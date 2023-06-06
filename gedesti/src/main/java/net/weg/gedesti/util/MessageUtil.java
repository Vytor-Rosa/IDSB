package net.weg.gedesti.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.weg.gedesti.dto.HistoricalDTO;
import net.weg.gedesti.dto.MessageDTO;
import net.weg.gedesti.model.entity.Historical;
import net.weg.gedesti.model.entity.Message;
import org.springframework.beans.BeanUtils;

public class MessageUtil {
    private ObjectMapper objectMapper = new ObjectMapper();

    public Message convertJsonToModel(String messageJson) {
        MessageDTO messageDTO = convetJsonToDTO(messageJson);
        return convertDtoToModel(messageDTO);
    }

    private MessageDTO convetJsonToDTO(String messageJson) {
        try {
            return objectMapper.readValue(messageJson, MessageDTO.class);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    private Message convertDtoToModel(MessageDTO messageDTO) {
        Message message = new Message();
        BeanUtils.copyProperties(messageDTO, message);
        return message;
    }
}
