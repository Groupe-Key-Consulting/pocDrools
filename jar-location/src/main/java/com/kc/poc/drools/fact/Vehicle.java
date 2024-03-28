package com.kc.poc.drools.fact;

import com.kc.poc.drools.service.ContractualYear;
import com.kc.poc.drools.service.VehicleYearData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

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
    private Map<Integer, VehicleYearData> yearData = new HashMap<>();

    public boolean isNewVehicle() {
        return newVehicle != null && newVehicle;
    }

    public BigDecimal getAllocationPercentage(int yearNumber) {
        BigDecimal allocationPercentage = null;
        if (yearData.get(yearNumber) != null) {
            allocationPercentage = yearData.get(yearNumber).getAllocationPercentage();
        }
        return allocationPercentage;
    }

    public BigDecimal calculateGrantAmortization(GrantType grantType, ContractualYear year, BigDecimal amortizationDuration, BigDecimal contractualNetValueStartYear) {
        // Différents de la methode original
        return amortizationDuration.multiply(contractualNetValueStartYear);
    }

    public BigDecimal calculateGrantAmortizationRemainsStartYear(GrantType grantType, ContractualYear year, BigDecimal grantAmortizationRemainsStartYearPreviousYear, BigDecimal grantAmortizationPreviousYear, BigDecimal contractualNetValueStartYear) {
        // Différents de la methode original
        return grantAmortizationRemainsStartYearPreviousYear.subtract(grantAmortizationPreviousYear).add(contractualNetValueStartYear);
    }

    public enum GrantType {
        STIF,
        OTHER
    }

}
