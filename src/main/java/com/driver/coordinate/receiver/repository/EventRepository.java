package com.driver.coordinate.receiver.repository;


import com.driver.coordinate.receiver.model.Event;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EventRepository extends MongoRepository<Event, String> {

}
