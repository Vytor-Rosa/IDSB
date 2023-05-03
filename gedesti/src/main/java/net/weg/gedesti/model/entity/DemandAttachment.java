package net.weg.gedesti.model.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "demandAttachment")
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Data
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
    @Column(length=100000000)
    private byte[] dice;

    public DemandAttachment(Demand demandMapper, byte[] arquivoBytes) {
    }
}
