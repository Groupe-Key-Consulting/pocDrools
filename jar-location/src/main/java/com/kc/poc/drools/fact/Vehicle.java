package com.kc.poc.drools.fact;

import com.kc.poc.drools.model.ModelType;
import lombok.Getter;
import lombok.Setter;
import org.apache.poi.ss.formula.functions.T;

import java.math.BigDecimal;
import java.time.LocalDate;

//@Entity
//@Table(name = "T_VEHICLE")
@Getter
@Setter
public class Vehicle {

    //    privat
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
