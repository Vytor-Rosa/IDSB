package net.weg.gedesti.dto;

import lombok.Data;
import net.weg.gedesti.model.entity.Chat;
import net.weg.gedesti.model.entity.Funcionario;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class MensagemDTO {
    private Integer messageCode;
    @NotBlank
    private String message;
    @NotNull
    private Date dateMessage;
    @NotBlank
    private String messageTime;
    @NotNull
    private Chat chat;
    @NotNull
    private Funcionario worker;
}
