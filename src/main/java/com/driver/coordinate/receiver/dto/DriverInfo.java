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

    public boolean isHardAcceleration() {
        return transmissionReasonId == 104 && acceleration > 0.22;
    }

    public boolean isHardBraking() {
        return transmissionReasonId == 105 && acceleration > 0.17;
    }

    public boolean isHardTurning() {
        return transmissionReasonId == 106 && acceleration > 0.30;
    }

    public boolean isSpeeding() {
        return speed > 80;
    }

    public boolean isOverTemperature() {
        return temperature > 115;
    }

    public boolean isLowBattery() {
        return batteryVoltage > 21;
    }
}
