package net.weg.gedesti.model.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "demandAttachment")
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class DemandAttachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Integer attachmentCodeDemand;

    @NonNull
    private String name;

    @NonNull
    private String type;

    @Lob
    @NonNull
    private byte[] dice;

}