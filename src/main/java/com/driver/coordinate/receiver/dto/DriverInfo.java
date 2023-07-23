package com.driver.coordinate.receiver.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DriverInfo {
    private String vehiclePlate;
    private String coordinateDate;
    private long latitude;
    private long longitude;
    private long hodometer;
    private double batteryVoltage;
    private double speed;
    private double acceleration;
    private int rpm;
    private boolean ignition;
    private double temperature;
    private int packetCounter;
    private int transmissionReasonId;
}
