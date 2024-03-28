package com.kc.poc.drools.fact;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Vehicle {
    private LocalDate purchaseDate;
    private Boolean newVehicle;
    private LocalDate startDate;
    private int newVehiclesAmortizationPeriod = 0;
    private Integer oldVehiclesAmortizationPeriod = 0;
    private double amortizationDuration = 0;

    public boolean isNewVehicle() {
        return newVehicle != null && newVehicle;
    }
}
