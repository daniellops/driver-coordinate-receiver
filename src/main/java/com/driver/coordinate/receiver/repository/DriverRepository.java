package com.driver.coordinate.receiver.repository;

import com.driver.coordinate.receiver.dto.DriverInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DriverRepository extends MongoRepository<DriverInfo, String> {

}
