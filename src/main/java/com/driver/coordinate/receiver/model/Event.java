package com.driver.coordinate.receiver.model;

import lombok.*;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@NoArgsConstructor
public class Event {
    @Id
    private String id;
    private String vehiclePlate;
    private String openedDate;
    private String closedDate;
    private long latitude;
    private long longitude;
    private EventType eventType;
    private double speed;

}
