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

    // calculateGrantAmortizationRemainsEndYearJava
    private BigDecimal allocationPercentage = BigDecimal.ZERO;

    public enum GrantType {
        STIF,
        OTHER
    }

    private ModelType type;
    private T strategy;

    public boolean isNewVehicle() {
        return newVehicle != null && newVehicle;
    }

    /**
     * Amortissement / reprise de la subvention avec affectation
     */
    public BigDecimal calculateGrantAmortization(GrantType grantType, ContractualYear year, BigDecimal amortizationDuration, BigDecimal contractualNetValueStartYear) {
//        return this.getStrategy(this.getType().calculateGrantAmortization(this, grantType, year, amortizationDuration, contractualNetValueStartYear);
        return null;
    }

    /**
     * Reste à amortir / à reprendre de la subvention au début de l'année
     */
    public BigDecimal calculateGrantAmortizationRemainsStartYear(GrantType grantType, ContractualYear year,
                                                                 BigDecimal grantAmortizationRemainsStartYearPreviousYear, BigDecimal grantAmortizationPreviousYear, BigDecimal contractualNetValueStartYear) {
//        return this.getStrategy(this.getProposition()).calculateGrantAmortizationRemainsStartYear(this, grantType, year, grantAmortizationRemainsStartYearPreviousYear,
//                grantAmortizationPreviousYear, contractualNetValueStartYear);
        return null;
    }
}
