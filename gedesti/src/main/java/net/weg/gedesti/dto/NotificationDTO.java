package net.weg.gedesti.dto;

import lombok.Data;
import net.weg.gedesti.model.entity.Worker;

import java.util.Date;

@Data
public class NotificationDTO {

    private Integer notificationCode;

    private String description;

    private Date date;

    private String icon;

    private Worker worker;

}
