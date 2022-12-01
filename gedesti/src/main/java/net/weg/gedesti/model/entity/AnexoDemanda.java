package net.weg.gedesti.model.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "anexoDemanda")
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class AnexoDemanda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Integer attachedCodeDemand;

    @NonNull
    private String name;

    @NonNull
    private String type;

    @Lob
    @NonNull
    private byte[] dice;

}
