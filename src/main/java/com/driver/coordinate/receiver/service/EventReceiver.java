package com.driver.coordinate.receiver.service;

import com.driver.coordinate.receiver.dto.DriverInfo;
import com.driver.coordinate.receiver.model.Event;
import com.driver.coordinate.receiver.model.EventType;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class EventReceiver {
    @Autowired
    EventService eventService;

    @RabbitListener(queues = {"${events.queue}"})
    public void receive(DriverInfo driverInfo) {
        System.out.println("Message: " + driverInfo.getVehiclePlate());

        List<Event> events = eventService.findOpenedEventsByVehicle(driverInfo.getVehiclePlate());

        saveEventWhenNecessary(events, driverInfo, EventType.HARD_ACCELERATION);
        saveEventWhenNecessary(events, driverInfo, EventType.HARD_BRAKING);
        saveEventWhenNecessary(events, driverInfo, EventType.HARD_TURNING);
        saveEventWhenNecessary(events, driverInfo, EventType.SPEEDING);
        saveEventWhenNecessary(events, driverInfo, EventType.OVER_TEMPERATURE);
        saveEventWhenNecessary(events, driverInfo, EventType.LOW_BATTERY);
    }

    public Event findEventByEventType(List<Event> events, EventType eventType) {
        Optional<Event> event = events.stream()
                .filter(e -> e.getEventType() == eventType)
                .findFirst();
        if (event.isPresent())
            return event.get();
        return null;
    }

    public void saveEventWhenNecessary(List<Event> events, DriverInfo driverInfo, EventType eventType){
        Event event = findEventByEventType(events, eventType);
        boolean condition = getConditionByEventType(driverInfo, eventType);

        if (event == null && condition) {
            createEvent(driverInfo, eventType);
        }
        if (event != null && !condition) {
            event.setClosedDate(LocalDateTime.now().toString());
            saveEvent(event);
        }
    }

    private boolean getConditionByEventType(DriverInfo driverInfo, EventType eventType) {
        return switch (eventType) {
            case HARD_ACCELERATION -> driverInfo.hasHardAcceleration();
            case HARD_BRAKING -> driverInfo.hasHardBraking();
            case HARD_TURNING -> driverInfo.hasHardTurning();
            case SPEEDING -> driverInfo.hasSpeeding();
            case OVER_TEMPERATURE -> driverInfo.hasOverTemperature();
            case LOW_BATTERY -> driverInfo.hasLowBattery();
        };
    }

    public void createEvent(DriverInfo driverInfo, EventType eventType){
        Event event = new Event();
        event.setEventType(eventType);
        event.setVehiclePlate(driverInfo.getVehiclePlate());
        event.setLatitude(driverInfo.getLatitude());
        event.setLongitude(driverInfo.getLongitude());
        event.setSpeed(driverInfo.getSpeed());
        event.setOpenedDate(LocalDateTime.now().toString());
        saveEvent(event);
    }

    public void saveEvent(Event event) {
        eventService.save(event);
    }
}
