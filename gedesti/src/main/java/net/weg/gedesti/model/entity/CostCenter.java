package net.weg.gedesti.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "costcenter")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CostCenter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Integer costCenterCode;

    @Column(nullable = false)
    private String costCenter;

}
