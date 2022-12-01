package net.weg.gedesti.model.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "anexo")
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class Anexo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Integer attachmentCode;

    @NonNull
    private String name;

    @NonNull
    private String type;

    @Lob
    @NonNull
    private byte[] dice;
}
