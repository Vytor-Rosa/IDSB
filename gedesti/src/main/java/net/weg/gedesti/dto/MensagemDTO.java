package net.weg.gedesti.dto;

import lombok.Data;
import net.weg.gedesti.model.entity.Chat;
import net.weg.gedesti.model.entity.Funcionario;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class MensagemDTO {
    private Integer codigoMensagem;
    @NotBlank
    private String mensagem;
    @NotNull
    private Date dataMensagem;
    @NotBlank
    private String horaMensagem;
    @NotNull
    private Chat chat;
    @NotNull
    private Funcionario funcionario;
}
