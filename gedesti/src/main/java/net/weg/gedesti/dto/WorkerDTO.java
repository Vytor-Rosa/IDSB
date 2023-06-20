package net.weg.gedesti.dto;

import lombok.Data;
import net.weg.gedesti.model.entity.Attachment;

import javax.persistence.Column;
import javax.validation.constraints.*;

@Data
public class WorkerDTO {

    @Positive
    private Integer workerCode;

    private String workerName;
    @Email
    private String corporateEmail;
    @NotBlank
    private String workerPassword;

    private String workerOffice;

    private String language;

    private String department;

    private Boolean voiceCommand;

    private Boolean pounds;

    private Boolean screenReader;

    private Boolean darkmode;

    private Boolean square;

    private Integer fontSize;


}
