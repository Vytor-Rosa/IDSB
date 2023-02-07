package net.weg.gedesti.dto;

import lombok.Data;
import lombok.ToString;
import net.weg.gedesti.model.entity.Bu;
import net.weg.gedesti.model.entity.Worker;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@Data
@ToString
public class ClassificationDTO {
//    private Integer classificationCode;

//    @NotNull
    private String classificationSize;

//    @NotBlank
    private String itSection;

//    @NotNull
//    @Positive
    private String ppmCode;

//    @NotBlank
    private String epicJiraLink;

//    @NotNull
    private Bu requesterBu;

//    @NotNull
    List<Bu> beneficiaryBu;

    private String deadline;

//    @NotNull
    private Worker analistRegistry;
}
