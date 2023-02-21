package net.weg.gedesti.dto;

import lombok.Data;

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
}
