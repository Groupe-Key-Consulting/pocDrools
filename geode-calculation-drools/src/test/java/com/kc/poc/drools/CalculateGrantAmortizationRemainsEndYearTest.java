package com.kc.poc.drools;

import com.kc.poc.drools.fact.Vehicle;
import com.kc.poc.drools.service.ContractualYear;
import com.kc.poc.drools.strategies.VehicleStrategy2021;
import com.kc.poc.drools.service.VehicleYearData;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.kie.api.command.Command;
import org.kie.internal.command.CommandFactory;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.kc.poc.drools.fact.Vehicle.GrantType.STIF;

public class CalculateGrantAmortizationRemainsEndYearTest {

    @Test
    public void should_calculate_grant_amortization_remains_end_year_in_java_when_() {
        // Given
        LocalDate date = LocalDate.now();

        VehicleYearData vehicleYearData = new VehicleYearData();
        vehicleYearData.setYear(2021);
        vehicleYearData.setAllocationPercentage(BigDecimal.valueOf(0.5));

        ContractualYear contractualYear = new ContractualYear();
        contractualYear.setNumber(2021);
        contractualYear.setYearNumber(2021);

        Vehicle vehicle = new Vehicle();
        vehicle.setNewVehicle(true);
        vehicle.setPurchaseDate(date.minusYears(2));
        vehicle.setStartDate(date.minusYears(1));
        vehicle.setNewVehiclesAmortizationPeriod(5);
        vehicle.setOldVehiclesAmortizationPeriod(0);
        vehicle.setYearData(Map.of(2021, vehicleYearData));

//        BigDecimal amortizationDuration = new VehicleStrategy2021().calculationTimeOfAmortizationDurationJava(vehicle);
        BigDecimal amortizationDuration = BigDecimal.valueOf(5);
        BigDecimal contractualNetValueStartYear = BigDecimal.valueOf(1000.5);
        BigDecimal grantAmortizationRemainsStartYearPreviousYear = BigDecimal.valueOf(500.0);
        BigDecimal grantAmortizationPreviousYear = BigDecimal.valueOf(300.0);

        double grantAmortization = vehicle.calculateGrantAmortization(STIF, contractualYear, amortizationDuration, contractualNetValueStartYear).doubleValue();
        double grantAmortizationRemainsStartYear = vehicle.calculateGrantAmortizationRemainsStartYear(STIF, contractualYear, grantAmortizationRemainsStartYearPreviousYear,
                grantAmortizationPreviousYear, contractualNetValueStartYear).doubleValue();

        // When
        BigDecimal result = new VehicleStrategy2021().calculateGrantAmortizationRemainsEndYearJava(vehicle, STIF, contractualYear, grantAmortizationRemainsStartYear, grantAmortization);

        // Then
        System.out.println("[Java Calculation] Grant Amortization: " + result);
    }


    @Test
    public void should_get_calculation_time_of_grant_amortization_remains_end_year_by_batch_of_million_vehicle_in_java() {
        // Given
        LocalDate date = LocalDate.now();
        List<Vehicle> vehicles = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < 1000000; i++) {
            boolean oldVehicle = random.nextBoolean();

            VehicleYearData vehicleYearData = new VehicleYearData();
            vehicleYearData.setYear(2021);
            vehicleYearData.setAllocationPercentage(BigDecimal.valueOf(random.nextDouble()));

            Vehicle vehicle = new Vehicle();
            vehicle.setNewVehicle(oldVehicle);
            vehicle.setPurchaseDate(date.minusYears(2).minusMonths(random.nextInt(12)));
            vehicle.setStartDate(date.minusYears(1).minusMonths(random.nextInt(12)));
            vehicle.setNewVehiclesAmortizationPeriod(oldVehicle ? 0 : random.nextInt(5));
            vehicle.setOldVehiclesAmortizationPeriod(oldVehicle ? random.nextInt(5) : 0);
            vehicle.setYearData(Map.of(2021, vehicleYearData));

            vehicles.add(vehicle);
        }

        LocalDateTime startDate = LocalDateTime.now();
        for (Vehicle vehicle : vehicles) {
            ContractualYear contractualYear = new ContractualYear();
            contractualYear.setNumber(2021);
            contractualYear.setYearNumber(2021);

            BigDecimal amortizationDuration = BigDecimal.valueOf(random.nextDouble() * 1000);
            BigDecimal contractualNetValueStartYear = BigDecimal.valueOf(random.nextDouble() * 1000);
            BigDecimal grantAmortizationRemainsStartYearPreviousYear = BigDecimal.valueOf(random.nextDouble() * 1000);
            BigDecimal grantAmortizationPreviousYear = BigDecimal.valueOf(random.nextDouble() * 1000);

            double grantAmortization = vehicle.calculateGrantAmortization(STIF, contractualYear, amortizationDuration, contractualNetValueStartYear).doubleValue();
            double grantAmortizationRemainsStartYear = vehicle.calculateGrantAmortizationRemainsStartYear(STIF, contractualYear, grantAmortizationRemainsStartYearPreviousYear,
                    grantAmortizationPreviousYear, contractualNetValueStartYear).doubleValue();

            BigDecimal result = new VehicleStrategy2021().calculateGrantAmortizationRemainsEndYearJava(vehicle, STIF, contractualYear, grantAmortizationRemainsStartYear, grantAmortization);
        }
        LocalDateTime endDate = LocalDateTime.now();

        // Then
        System.out.println("Calculation time of grant amortization remains end year for 1.000.000 Object in Java: " + Duration.between(startDate, endDate).toMillis());
    }

    @Test
    public void should_calculate_grant_amortization_remains_end_year_in_drools_when_() {
        // Given
        LocalDate date = LocalDate.now();

        VehicleYearData vehicleYearData = new VehicleYearData();
        vehicleYearData.setYear(2021);
        vehicleYearData.setAllocationPercentage(BigDecimal.valueOf(0.5));

        ContractualYear contractualYear = new ContractualYear();
        contractualYear.setNumber(2021);
        contractualYear.setYearNumber(2021);

        Vehicle vehicle = new Vehicle();
        vehicle.setNewVehicle(true);
        vehicle.setPurchaseDate(date.minusYears(2));
        vehicle.setStartDate(date.minusYears(1));
        vehicle.setNewVehiclesAmortizationPeriod(5);
        vehicle.setOldVehiclesAmortizationPeriod(0);
        vehicle.setYearData(Map.of(2021, vehicleYearData));

        BigDecimal amortizationDuration = new VehicleStrategy2021().calculationTimeOfAmortizationDurationJava(vehicle);
        BigDecimal contractualNetValueStartYear = BigDecimal.valueOf(1000.5);
        BigDecimal grantAmortizationRemainsStartYearPreviousYear = BigDecimal.valueOf(500.0);
        BigDecimal grantAmortizationPreviousYear = BigDecimal.valueOf(300.0);

        // When
        BigDecimal result = new VehicleStrategy2021().calculateGrantAmortizationRemainsEndYearDrools(vehicle, STIF, contractualYear, amortizationDuration, grantAmortizationRemainsStartYearPreviousYear, grantAmortizationPreviousYear, contractualNetValueStartYear);

        // Then
        System.out.println("[Drools Calculation] Grant Amortization: " + result);
    }

//    @Test
//    public void should_get_calculation_time_of_grant_amortization_remains_end_year_by_batch_of_million_vehicle_in_drools() {
//        // Given
//        LocalDate date = LocalDate.now();
//        List<Vehicle> vehicles = new ArrayList<>();
//        Random random = new Random();
//
//        for (int i = 0; i < 1000000; i++) {
//            boolean oldVehicle = random.nextBoolean();
//
//            VehicleYearData vehicleYearData = new VehicleYearData();
//            vehicleYearData.setYear(2021);
//            vehicleYearData.setAllocationPercentage(BigDecimal.valueOf(random.nextDouble()));
//
//            Vehicle vehicle = new Vehicle();
//            vehicle.setNewVehicle(oldVehicle);
//            vehicle.setPurchaseDate(date.minusYears(2).minusMonths(random.nextInt(12)));
//            vehicle.setStartDate(date.minusYears(1).minusMonths(random.nextInt(12)));
//            vehicle.setNewVehiclesAmortizationPeriod(oldVehicle ? 0 : random.nextInt(5));
//            vehicle.setOldVehiclesAmortizationPeriod(oldVehicle ? random.nextInt(5) : 0);
//            vehicle.setYearData(Map.of(2021, vehicleYearData));
//
//            vehicles.add(vehicle);
//        }
//
//        LocalDateTime startDate = LocalDateTime.now();
//        for (Vehicle vehicle : vehicles) {
//            ContractualYear contractualYear = new ContractualYear();
//            contractualYear.setNumber(2021);
//            contractualYear.setYearNumber(2021);
//
//            BigDecimal amortizationDuration = BigDecimal.valueOf(random.nextDouble() * 1000);
//            BigDecimal contractualNetValueStartYear = BigDecimal.valueOf(random.nextDouble() * 1000);
//            BigDecimal grantAmortizationRemainsStartYearPreviousYear = BigDecimal.valueOf(random.nextDouble() * 1000);
//            BigDecimal grantAmortizationPreviousYear = BigDecimal.valueOf(random.nextDouble() * 1000);
//
//            BigDecimal result = new VehicleStrategy2021().calculateGrantAmortizationRemainsEndYearDrools(vehicle, STIF, contractualYear, amortizationDuration, grantAmortizationRemainsStartYearPreviousYear, grantAmortizationPreviousYear, contractualNetValueStartYear);
//        }
//        LocalDateTime endDate = LocalDateTime.now();
//
//        // Then
//        System.out.println("Calculation time of grant amortization remains end year for 1.000.000 Object in Drools: " + Duration.between(startDate, endDate).toMillis());
//    }

    @Test
    @Disabled
    public void should_get_calculation_time_of_grant_amortization_remains_end_year_by_batch_of_million_vehicle_in_drools_in_stateful_session_with_given() {
        // Given
        LocalDate date = LocalDate.now();
        List<Vehicle> vehicles = new ArrayList<>();
        List<ContractualYear> contractualYears = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < 1000000; i++) {
            boolean oldVehicle = random.nextBoolean();

            VehicleYearData vehicleYearData = new VehicleYearData();
            vehicleYearData.setYear(2021);
            vehicleYearData.setAllocationPercentage(BigDecimal.valueOf(random.nextDouble()));

            vehicles.add(new Vehicle(
                    date.minusYears(2).minusMonths(random.nextInt(12)),
                    oldVehicle,
                    date.minusYears(1).minusMonths(random.nextInt(12)),
                    oldVehicle ? 0 : random.nextInt(5),
                    oldVehicle ? random.nextInt(5) : 0,
                    0,
                    Map.of(2021, vehicleYearData),
                    null

            ));

            contractualYears.add(new ContractualYear(
                    date.minusYears(2).minusMonths(random.nextInt(12)).getYear(),
                    date.minusYears(1).minusMonths(random.nextInt(12)).getYear()
            ));
        }
        BigDecimal amortizationDuration = BigDecimal.valueOf(random.nextDouble() * 1000);
        BigDecimal contractualNetValueStartYear = BigDecimal.valueOf(random.nextDouble() * 1000);
        BigDecimal grantAmortizationRemainsStartYearPreviousYear = BigDecimal.valueOf(random.nextDouble() * 1000);
        BigDecimal grantAmortizationPreviousYear = BigDecimal.valueOf(random.nextDouble() * 1000);

        // When
        long duration = new VehicleStrategy2021().calculationTimeOfGrantAmortizationRemainsEndYearDroolsStateful_ImportedObjects(vehicles, STIF, contractualYears, amortizationDuration, grantAmortizationRemainsStartYearPreviousYear, grantAmortizationPreviousYear, contractualNetValueStartYear);

        // Then
        System.out.println("Calculation time of grant amortization remains end year for 1.000.000 Object in Drools: " + duration);
    }

    @Test
    @Disabled
    public void should_get_calculation_time_of_grant_amortization_remains_end_year_by_batch_of_million_vehicle_in_drools_in_stateless_session_with_given() {
        // Given
        LocalDate date = LocalDate.now();
        List<Command> cmd = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < 1000000; i++) {
            boolean oldVehicle = random.nextBoolean();

            VehicleYearData vehicleYearData = new VehicleYearData();
            vehicleYearData.setYear(2021);
            vehicleYearData.setAllocationPercentage(BigDecimal.valueOf(random.nextDouble()));

            cmd.add(CommandFactory.newInsert(new Vehicle(
                    date.minusYears(2).minusMonths(random.nextInt(12)),
                    oldVehicle,
                    date.minusYears(1).minusMonths(random.nextInt(12)),
                    oldVehicle ? 0 : random.nextInt(5),
                    oldVehicle ? random.nextInt(5) : 0,
                    0,
                    Map.of(2021, vehicleYearData),
                    null
            )));

            cmd.add(CommandFactory.newInsert(new ContractualYear(
                    date.minusYears(2).minusMonths(random.nextInt(12)).getYear(),
                    date.minusYears(1).minusMonths(random.nextInt(12)).getYear()
            )));
        }
        BigDecimal amortizationDuration = BigDecimal.valueOf(random.nextDouble() * 1000);
        BigDecimal contractualNetValueStartYear = BigDecimal.valueOf(random.nextDouble() * 1000);
        BigDecimal grantAmortizationRemainsStartYearPreviousYear = BigDecimal.valueOf(random.nextDouble() * 1000);
        BigDecimal grantAmortizationPreviousYear = BigDecimal.valueOf(random.nextDouble() * 1000);

        // When
        long duration = new VehicleStrategy2021().calculationTimeOfGrantAmortizationRemainsEndYearDroolsStateless_ImportedObjects(cmd, STIF, amortizationDuration, grantAmortizationRemainsStartYearPreviousYear, grantAmortizationPreviousYear, contractualNetValueStartYear);

        // Then
        System.out.println("Calculation time of grant amortization remains end year for 1.000.000 Object in Drools: " + duration);
    }
}
