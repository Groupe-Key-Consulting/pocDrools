package com.kc.poc.drools;

import com.kc.poc.drools.fact.Vehicle;
import com.kc.poc.drools.strategies.VehicleStrategy2021;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculateAmortizationDurationTest {

    @Test
    public void should_calculate_amortization_duration_when_new_vehicle_is_true_and_amortization_period_is_greater_than_zero() {
        // Given
        LocalDate date = LocalDate.now();
        Vehicle vehicle = new Vehicle();
        vehicle.setNewVehicle(true);
        vehicle.setPurchaseDate(date.minusYears(2));
        vehicle.setStartDate(date.minusYears(1));
        vehicle.setNewVehiclesAmortizationPeriod(5);
        vehicle.setOldVehiclesAmortizationPeriod(0);
        boolean test = vehicle.getPurchaseDate().plusMonths(vehicle.getNewVehiclesAmortizationPeriod() * 12L).isAfter(vehicle.getStartDate());

        // When
        BigDecimal result = new VehicleStrategy2021().calculateAmortizationDurationDrools(vehicle);

        // Then
        assertEquals(BigDecimal.valueOf(5.0), result);
    }

    @Test
    public void should_calculate_amortization_duration_when_new_vehicle_is_true_and_amortization_period_is_zero() {
        // Given
        LocalDate date = LocalDate.now();
        Vehicle vehicle = new Vehicle();
        vehicle.setNewVehicle(true);
        vehicle.setPurchaseDate(date.minusYears(2));
        vehicle.setStartDate(date.minusYears(1));
        vehicle.setNewVehiclesAmortizationPeriod(0);
        vehicle.setOldVehiclesAmortizationPeriod(0);
        boolean test = vehicle.getPurchaseDate().plusMonths(vehicle.getNewVehiclesAmortizationPeriod() * 12L).isAfter(vehicle.getStartDate());

        // When
        BigDecimal result = new VehicleStrategy2021().calculateAmortizationDurationDrools(vehicle);

        // Then
        assertEquals(BigDecimal.valueOf(0.0), result);
    }

    @Test
    public void should_calculate_amortization_duration_when_new_vehicle_is_false_and_amortization_period_is_greater_than_zero() {
        // Given
        LocalDate date = LocalDate.now();
        Vehicle vehicle = new Vehicle();
        vehicle.setNewVehicle(false);
        vehicle.setPurchaseDate(date.minusYears(2));
        vehicle.setStartDate(date.minusYears(1));
        vehicle.setNewVehiclesAmortizationPeriod(0);
        vehicle.setOldVehiclesAmortizationPeriod(-2);
        boolean test = vehicle.getPurchaseDate().plusMonths(vehicle.getNewVehiclesAmortizationPeriod() * 12L).isAfter(vehicle.getStartDate());

        // When
        BigDecimal result = new VehicleStrategy2021().calculateAmortizationDurationDrools(vehicle);

        // Then
        assertEquals(BigDecimal.valueOf(0.0), result);
    }
}
