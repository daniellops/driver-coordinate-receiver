package com.driver.coordinate.receiver.service;

import com.driver.coordinate.receiver.dto.DriverInfo;
import com.driver.coordinate.receiver.model.Event;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EventReceiver {
    @Autowired
    EventService eventService;

    @RabbitListener(queues = {"${events.queue}"})
    public void receive(DriverInfo driverInfo) {
        System.out.println("Message: " + driverInfo.getVehiclePlate());

        Event e = new Event();
        e.setVehiclePlate(driverInfo.getVehiclePlate());
        eventService.save(e);
    }
}
