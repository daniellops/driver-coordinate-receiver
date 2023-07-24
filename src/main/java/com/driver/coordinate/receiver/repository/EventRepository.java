package com.driver.coordinate.receiver.repository;


import com.driver.coordinate.receiver.model.Event;
import com.driver.coordinate.receiver.model.EventType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface EventRepository extends MongoRepository<Event, String> {

    @Query("{'vehiclePlate' : ?0, 'closedDate': null}")
    List<Event> findOpenedEventsByVehicle(String vehiclePlate);

}
