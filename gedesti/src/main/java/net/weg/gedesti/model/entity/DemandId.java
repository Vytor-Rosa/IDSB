package net.weg.gedesti.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DemandId implements Serializable {
    private Integer demandCode;
    private Integer demandVersion;
}
