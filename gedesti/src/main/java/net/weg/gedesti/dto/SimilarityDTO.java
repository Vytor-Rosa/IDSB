package net.weg.gedesti.dto;

import lombok.Data;
import net.weg.gedesti.model.entity.Demand;

@Data
public class SimilarityDTO {

    private Demand demandOne;
    private Demand demandTwo;
    private Double similarityValue;
}
