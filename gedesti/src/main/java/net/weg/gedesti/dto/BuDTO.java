package net.weg.gedesti.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class BuDTO {
    private Integer buCode;

    @NotBlank
    private String bu;
}
