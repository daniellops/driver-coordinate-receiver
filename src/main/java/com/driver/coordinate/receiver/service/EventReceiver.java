package com.driver.coordinate.receiver.service;

import com.driver.coordinate.receiver.dto.DriverInfo;
import com.driver.coordinate.receiver.model.Event;
import com.driver.coordinate.receiver.model.EventType;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Component
public class EventReceiver {
    @Autowired
    EventService eventService;

    @RabbitListener(queues = {"${events.queue}"})
    public void receive(DriverInfo driverInfo) {
        System.out.println("Message: " + driverInfo.getVehiclePlate());

        eventService.processEvents(driverInfo);
    }
}
