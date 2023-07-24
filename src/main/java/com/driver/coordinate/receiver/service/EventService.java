package com.driver.coordinate.receiver.service;

import com.driver.coordinate.receiver.model.Event;
import com.driver.coordinate.receiver.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

}
