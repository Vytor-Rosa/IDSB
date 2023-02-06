package net.weg.gedesti.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "reproach")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Reproach {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reproachCode;

    @NotNull
    private String reproachDescription;
}
