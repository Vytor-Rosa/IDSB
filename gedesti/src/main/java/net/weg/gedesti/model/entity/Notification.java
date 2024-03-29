package net.weg.gedesti.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.jdbc.Work;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "notification")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Integer notificationCode;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String date;

    @Column(nullable = false)
    private String icon;

    @OneToOne
    @JoinColumn(name = "worker_id")
    private Worker worker;

    @Column(nullable = true)
    private boolean visualized;

    @Column(nullable = false)
    private String type;


}
