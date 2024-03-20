package com.kc.poc.drools.fact;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

//@Entity
//@Table(name = "T_VEHICLE")
@Getter
@Setter
public class Vehicle {

//    private VehicleType type;
    private LocalDate purchaseDate;
    private Boolean newVehicle;
    private LocalDate startDate;
    private int newVehiclesAmortizationPeriod = 0;
    private int oldVehiclesAmortizationPeriod = 0;
    private double amortizationDuration = 0;

//    public VehicleType getType() {
//        return type;
//    }

    public boolean isNewVehicle() {
        return newVehicle != null && newVehicle;
    }
}
