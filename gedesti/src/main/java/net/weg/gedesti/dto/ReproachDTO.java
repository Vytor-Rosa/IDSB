package net.weg.gedesti.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ReproachDTO {
    private Integer reproachCode;
    @NotNull
    private String reproachDescription;

}
