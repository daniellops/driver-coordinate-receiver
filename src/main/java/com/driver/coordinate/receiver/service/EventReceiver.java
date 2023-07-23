package com.driver.coordinate.receiver.service;

import com.driver.coordinate.receiver.dto.DriverInfo;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class EventReceiver {
    @RabbitListener(queues = {"${events.queue}"})
    public void receive(DriverInfo message) {
        System.out.println("Message: " + message.getVehiclePlate());
    }
}
