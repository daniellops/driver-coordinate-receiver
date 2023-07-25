package com.driver.coordinate.receiver.service;

import com.driver.coordinate.receiver.dto.DriverInfo;
import com.driver.coordinate.receiver.model.Event;
import com.driver.coordinate.receiver.model.EventType;
import com.driver.coordinate.receiver.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class EventService {
    @Autowired
    private EventRepository eventRepository;

    public void save(Event event) {
        eventRepository.save(event);
    }

    public List<Event> findOpenedEventsByVehicle(String vehiclePlate) {
        return eventRepository.findOpenedEventsByVehicle(vehiclePlate);
    }

    public void processEvents(DriverInfo driverInfo) {
        List<Event> events = findOpenedEventsByVehicle(driverInfo.getVehiclePlate());
        Stream<EventType> eventTypes = Arrays.stream(EventType.values());
        eventTypes.forEach(eventType -> saveEventWhenNecessary(events, driverInfo, eventType));
    }

    public Event findEventByEventType(List<Event> events, EventType eventType) {
        Optional<Event> event = events.stream()
                .filter(e -> e.getEventType() == eventType)
                .findFirst();
        if (event.isPresent())
            return event.get();
        return null;
    }

    public void saveEventWhenNecessary(List<Event> events, DriverInfo driverInfo, EventType eventType) {
        Event event = findEventByEventType(events, eventType);
        boolean condition = getConditionByEventType(driverInfo, eventType);

        if (event == null && condition) {
            createEvent(driverInfo, eventType);
        }
        if (event != null && !condition) {
            event.setClosedDate(LocalDateTime.now().toString());
            save(event);
        }
    }

    private boolean getConditionByEventType(DriverInfo driverInfo, EventType eventType) {
        return switch (eventType) {
            case HARD_ACCELERATION -> driverInfo.isHardAcceleration();
            case HARD_BRAKING -> driverInfo.isHardBraking();
            case HARD_TURNING -> driverInfo.isHardTurning();
            case SPEEDING -> driverInfo.isSpeeding();
            case OVER_TEMPERATURE -> driverInfo.isOverTemperature();
            case LOW_BATTERY -> driverInfo.isLowBattery();
        };
    }

    public void createEvent(DriverInfo driverInfo, EventType eventType) {
        Event event = new Event();
        event.setEventType(eventType);
        event.setVehiclePlate(driverInfo.getVehiclePlate());
        event.setLatitude(driverInfo.getLatitude());
        event.setLongitude(driverInfo.getLongitude());
        event.setSpeed(driverInfo.getSpeed());
        event.setOpenedDate(LocalDateTime.now().toString());
        save(event);
    }
}
