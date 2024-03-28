package com.kc.poc.drools.strategies;

import com.kc.poc.drools.fact.Vehicle;
import com.kc.poc.drools.service.ContractualYear;

import java.math.BigDecimal;

public interface IVehicleStrategy {

//    BigDecimal calculateAmortizationDurationJava(Vehicle vehicle);

//    BigDecimal calculateContractualNetValueStartYear(Vehicle vehicle, ContractualYear year, BigDecimal contractualNetValueStartYearPreviousYear,
//                                                     BigDecimal damPreviousYear, BigDecimal stifGrantAmortizationPreviousYear, BigDecimal otherGrantAmortizationPreviousYear);
//
//    BigDecimal calculateAmortization(Vehicle vehicle, ContractualYear year, BigDecimal amortizationDuration, BigDecimal contractualNetValueStartYear);
//
//    @Deprecated
//    BigDecimal calculateFinancialFees(Vehicle vehicle, ContractualYear year, BigDecimal amortizationDuration, BigDecimal debtFinancingAverageRate,
//                                      BigDecimal contractualNetValueStartYearPreviousYear, BigDecimal damPreviousYear, //
//                                      BigDecimal stifGrantAmortizationRemainsStartYearPreviousYear, BigDecimal stifGrantAmortizationPreviousYear, //
//                                      BigDecimal otherGrantAmortizationRemainsStartYearPreviousYear, BigDecimal otherGrantAmortizationPreviousYear);
//
//    BigDecimal calculateFinancialFees(Vehicle vehicle, ContractualYear year, BigDecimal amortizationDuration, BigDecimal debtFinancingAverageRate,
//                                      BigDecimal contractualNetValueStartYearBigDecimal, BigDecimal damBigDecimal, BigDecimal stifGrantAmortizationRemainsStartYearBigDecimal,
//                                      BigDecimal otherGrantAmortizationRemainsStartYearBigDecimal);
//
//
//    BigDecimal calculateAge(Vehicle vehicle, ContractualYear year);
//
//    BigDecimal calculateGrantAmortizationRemainsStartYear(Vehicle vehicle, Vehicle.GrantType grantType, ContractualYear year,
//                                                          BigDecimal grantAmortizationRemainsStartYearPreviousYear, BigDecimal grantAmortizationPreviousYear, BigDecimal contractualNetValueStartYear);
//
    BigDecimal calculateGrantAmortizationRemainsEndYearJava(Vehicle vehicle, Vehicle.GrantType grantType, ContractualYear year, BigDecimal amortizationDuration,
                                                            BigDecimal grantAmortizationRemainsStartYearPreviousYear, BigDecimal grantAmortizationPreviousYear, BigDecimal contractualNetValueStartYear);

//    BigDecimal calculateDurationInContractualYear(Vehicle vehicle, ContractualYear year);
//
//    BigDecimal calculateGrantAmortization(Vehicle vehicle, Vehicle.GrantType grantType, ContractualYear year, BigDecimal amortizationDuration, BigDecimal contractualNetValueStartYear);
//
//    BigDecimal calculateUsageRatio(Vehicle vehicle, ContractualYear year);
//
//    boolean shouldUseEndYearAmortizationInsteadOfAmortizationIfLower();
}
