package com.kc.poc.drools.service;

import com.kc.poc.drools.strategies.IVehicleStrategy;
import com.kc.poc.drools.fact.Vehicle;
import info.stif.requeteweb.domain.vehicle.Vehicle;
import info.stif.requeteweb.service.contract.ContractualYear;
import com.kc.poc.drools.util.DateUtil;
import com.kc.poc.drools.util.MathUtil;
import org.joda.time.LocalDate;

import java.math.BigDecimal;

public class VehicleStrategy implements IVehicleStrategy {

    @Override
    public BigDecimal calculateAmortizationDuration(Vehicle vehicle) {
        double amortizationDuration = 0;
        if (vehicle.getType() != null && vehicle.isNewVehicle() && vehicle.getType().getNewVehiclesAmortizationPeriod() != null) {
            amortizationDuration = vehicle.getType().getNewVehiclesAmortizationPeriod();
        } else {
            if ( vehicle.getType() != null && vehicle.getPurchaseDate() != null && vehicle.getStartDate() != null && vehicle.getType().getOldVehiclesAmortizationPeriod() != null) {
                amortizationDuration = vehicle.getType().getOldVehiclesAmortizationPeriod() - DateUtil.days360Ratio(vehicle.getPurchaseDate(), vehicle.getStartDate());
            }
        }
        if (amortizationDuration < 0) {
            amortizationDuration = 0;
        }
        return MathUtil.asBigDecimal(amortizationDuration);
    }

//    //todo: le <= 1 est suspect
//    @Override
//    public BigDecimal calculateContractualNetValueStartYear(Vehicle vehicle, ContractualYear year, BigDecimal contractualNetValueStartYearPreviousYear,
//                                                            BigDecimal damPreviousYear, BigDecimal stifGrantAmortizationPreviousYear, BigDecimal otherGrantAmortizationPreviousYear) {
//        double contractualNetValueStartYear = 0;
//        Integer startDateValue = vehicle.getStartDateValue();
//        LocalDate contractStartDate = vehicle.getProposition().getStartDate();
//        // double durationInContractualYear = MathUtil.asDoubleValue(calculateDurationInContractualYear(year));
//        double durationInContract = MathUtil.asDoubleValue(vehicle.calculateDurationInContract(year));
//        double age = MathUtil.asDoubleValue(vehicle.calculateAge(year));
//        if (durationInContract != 0 && age > 0) {
//            //bug en prod ici ? - le todo concerne cette ligne
//            if ((!vehicle.isNewVehicle() && DateUtil.days360Ratio(contractStartDate, year.getEndDate()) <= 1) || durationInContract <= 1) {
//                contractualNetValueStartYear = startDateValue;
//            } else {
//                double allocationPercentagePreviousYear = MathUtil.asDoubleValue(vehicle.getAllocationPercentage(year.getNumber() - 1));
//                contractualNetValueStartYear = MathUtil.asDoubleValue(contractualNetValueStartYearPreviousYear)
//                        - (MathUtil.asDoubleValue(damPreviousYear) + MathUtil.asDoubleValue(otherGrantAmortizationPreviousYear)
//                        + MathUtil.asDoubleValue(stifGrantAmortizationPreviousYear)) / allocationPercentagePreviousYear;
//            }
//        }
//        return MathUtil.asBigDecimal(contractualNetValueStartYear);
//    }
//
//    @Override
//    public BigDecimal calculateAmortization(Vehicle vehicle, ContractualYear year, BigDecimal amortizationDuration, BigDecimal contractualNetValueStartYear) {
//        double amortization = 0;
//        LocalDate startDate = vehicle.getStartDate();
//        Integer startDateValue = vehicle.getStartDateValue();
//        if (startDate != null && startDateValue != null && amortizationDuration != null && amortizationDuration.doubleValue() != 0) {
//            double durationInContract = vehicle.calculateDurationInContract(year).doubleValue();
//            double durationInContractualYear = vehicle.calculateDurationInContractualYear(year).doubleValue();
//            if (startDate.isBefore(year.getEndDate()) && durationInContract < (amortizationDuration.doubleValue() + 1)) {
//                if (durationInContractualYear >= 1) {
//                    if (durationInContract <= amortizationDuration.doubleValue()) {
//                        amortization = startDateValue / amortizationDuration.doubleValue();
//                    } else {
//                        amortization = (1 - (durationInContract - amortizationDuration.doubleValue())) * startDateValue / amortizationDuration.doubleValue();
//                    }
//                } else {
//                    amortization = durationInContractualYear * startDateValue / amortizationDuration.doubleValue();
//                }
//            }
//        }
//        if (contractualNetValueStartYear != null && amortization > contractualNetValueStartYear.doubleValue()) {
//            amortization = contractualNetValueStartYear.doubleValue();
//        }
//        return MathUtil.asBigDecimal(amortization);
//    }
//
//    @Deprecated
//    @Override
//    public BigDecimal calculateFinancialFees(Vehicle vehicle, ContractualYear year, BigDecimal amortizationDuration, BigDecimal debtFinancingAverageRate, BigDecimal contractualNetValueStartYearPreviousYear, BigDecimal damPreviousYear, BigDecimal stifGrantAmortizationRemainsStartYearPreviousYear, BigDecimal stifGrantAmortizationPreviousYear, BigDecimal otherGrantAmortizationRemainsStartYearPreviousYear, BigDecimal otherGrantAmortizationPreviousYear) {
//        double financialFees = 0;
//        LocalDate startDate = vehicle.getStartDate();
//        double allocationPercentage = MathUtil.asDoubleValue(vehicle.getAllocationPercentage(year.getNumber()));
//        if (startDate != null) {
//            double durationInContract = MathUtil.asDoubleValue(vehicle.calculateDurationInContract(year));
//            double durationInContractualYear = vehicle.calculateDurationInContractualYear(year).doubleValue();
//            BigDecimal contractualNetValueStartYearBigDecimal = vehicle.calculateContractualNetValueStartYear(year, contractualNetValueStartYearPreviousYear,
//                    damPreviousYear, stifGrantAmortizationPreviousYear, otherGrantAmortizationPreviousYear);
//            double contractualNetValueStartYear = contractualNetValueStartYearBigDecimal.doubleValue();
//            double stifGrantAmortizationRemainsStartYear = vehicle.calculateStifGrantAmortizationRemainsStartYear(year,
//                    stifGrantAmortizationRemainsStartYearPreviousYear, stifGrantAmortizationPreviousYear, contractualNetValueStartYearBigDecimal).doubleValue();
//            double otherGrantAmortizationRemainsStartYear = vehicle.calculateOtherGrantAmortizationRemainsStartYear(year,
//                    otherGrantAmortizationRemainsStartYearPreviousYear, otherGrantAmortizationPreviousYear, contractualNetValueStartYearBigDecimal).doubleValue();
//            double dam = vehicle.calculateDam(year, amortizationDuration, contractualNetValueStartYearBigDecimal).doubleValue();
//            if (startDate.isBefore(year.getEndDate()) && durationInContract < (amortizationDuration.doubleValue() + 1)) {
//                if (durationInContractualYear >= 1) {
//                    if (durationInContract <= amortizationDuration.doubleValue()) {
//                        financialFees = (contractualNetValueStartYear - stifGrantAmortizationRemainsStartYear - otherGrantAmortizationRemainsStartYear
//                                - dam / (2 * allocationPercentage)) * debtFinancingAverageRate.doubleValue() * allocationPercentage;
//                    } else {
//                        financialFees = (1 - (durationInContract - amortizationDuration.doubleValue())) * (contractualNetValueStartYear
//                                - stifGrantAmortizationRemainsStartYear - otherGrantAmortizationRemainsStartYear - dam / (2 * allocationPercentage))
//                                * debtFinancingAverageRate.doubleValue() * allocationPercentage;
//                    }
//                } else {
//                    financialFees = durationInContractualYear * (contractualNetValueStartYear - stifGrantAmortizationRemainsStartYear
//                            - otherGrantAmortizationRemainsStartYear - dam / (2 * allocationPercentage)) * debtFinancingAverageRate.doubleValue()
//                            * allocationPercentage;
//                }
//            }
//        }
//        return MathUtil.asBigDecimal(financialFees);
//    }
//
//    @Override
//    public BigDecimal calculateFinancialFees(Vehicle vehicle, ContractualYear year, BigDecimal amortizationDuration, BigDecimal debtFinancingAverageRate, BigDecimal contractualNetValueStartYearBigDecimal, BigDecimal damBigDecimal, BigDecimal stifGrantAmortizationRemainsStartYearBigDecimal, BigDecimal otherGrantAmortizationRemainsStartYearBigDecimal) {
//        double financialFees = 0;
//        LocalDate startDate = vehicle.getStartDate();
//        double allocationPercentage = MathUtil.asDoubleValue(vehicle.getAllocationPercentage(year.getNumber()));
//        if (startDate != null) {
//            double durationInContract = MathUtil.asDoubleValue(vehicle.calculateDurationInContract(year));
//            double durationInContractualYear = vehicle.calculateDurationInContractualYear(year).doubleValue();
//            double contractualNetValueStartYear = contractualNetValueStartYearBigDecimal.doubleValue();
//            double stifGrantAmortizationRemainsStartYear = stifGrantAmortizationRemainsStartYearBigDecimal.doubleValue();
//            double otherGrantAmortizationRemainsStartYear = otherGrantAmortizationRemainsStartYearBigDecimal.doubleValue();
//            double dam = damBigDecimal.doubleValue();
//            if (startDate.isBefore(year.getEndDate()) && durationInContract < (amortizationDuration.doubleValue() + 1)) {
//                if (durationInContractualYear >= 1) {
//                    if (durationInContract <= amortizationDuration.doubleValue()) {
//                        financialFees = (contractualNetValueStartYear - stifGrantAmortizationRemainsStartYear - otherGrantAmortizationRemainsStartYear
//                                - dam / (2 * allocationPercentage)) * debtFinancingAverageRate.doubleValue() * allocationPercentage;
//                    } else {
//                        financialFees = (1 - (durationInContract - amortizationDuration.doubleValue())) * (contractualNetValueStartYear
//                                - stifGrantAmortizationRemainsStartYear - otherGrantAmortizationRemainsStartYear - dam / (2 * allocationPercentage))
//                                * debtFinancingAverageRate.doubleValue() * allocationPercentage;
//                    }
//                } else {
//                    financialFees = durationInContractualYear * (contractualNetValueStartYear - stifGrantAmortizationRemainsStartYear
//                            - otherGrantAmortizationRemainsStartYear - dam / (2 * allocationPercentage)) * debtFinancingAverageRate.doubleValue()
//                            * allocationPercentage;
//                }
//            }
//        }
//        return MathUtil.asBigDecimal(financialFees);
//    }
//
//    @Override
//    public BigDecimal calculateAge(Vehicle vehicle, ContractualYear year) {
//        BigDecimal age = BigDecimal.ZERO;
//        BigDecimal durationInContractualYear = vehicle.calculateDurationInContractualYear(year);
//        LocalDate startDate = vehicle.getPurchaseDate();
//        if (startDate != null) {
//            if (durationInContractualYear.compareTo(BigDecimal.ZERO) != 0) {
//                age = MathUtil.asBigDecimal(DateUtil.days360Ratio(startDate, year.getEndDate()));
//                if (age.compareTo(BigDecimal.ZERO) < 0) {
//                    age = BigDecimal.ZERO;
//                }
//            }
//        }
//        return age;
//    }
//
//    @Override
//    public BigDecimal calculateGrantAmortizationRemainsStartYear(Vehicle vehicle, Vehicle.GrantType grantType, ContractualYear year,
//                                                                 BigDecimal grantAmortizationRemainsStartYearPreviousYear, BigDecimal grantAmortizationPreviousYear, BigDecimal contractualNetValueStartYear) {
//        return this.calculateGrantAmortizationRemainsStartYear(vehicle, grantType, year, grantAmortizationRemainsStartYearPreviousYear, grantAmortizationPreviousYear);
//
//    }
//
//    private BigDecimal calculateGrantAmortizationRemainsStartYear(Vehicle vehicle, Vehicle.GrantType grantType, ContractualYear year,
//                                                                  BigDecimal grantAmortizationRemainsStartYearPreviousYear, BigDecimal grantAmortizationPreviousYear) {
//        double grantAmortizationRemainsStartYear = 0;
//        double durationInContractualYear = vehicle.calculateDurationInContractualYear(year).doubleValue();
//        double durationInContract = vehicle.calculateDurationInContract(year).doubleValue();
//        double grant = vehicle.getGrant(grantType).doubleValue();
//        LocalDate startDate = vehicle.getStartDate();
//        if (durationInContractualYear != 0) {
//            if (startDate.isBefore(year.getEndDate())) {
//                if (durationInContract <= 1) {
//                    grantAmortizationRemainsStartYear = grant;
//                } else {
//                    double allocationPercentage = MathUtil.asDoubleValue(vehicle.getAllocationPercentage(year.getNumber() - 1));
//                    grantAmortizationRemainsStartYear = MathUtil.asDoubleValue(grantAmortizationRemainsStartYearPreviousYear)
//                            - MathUtil.asDoubleValue(grantAmortizationPreviousYear) / allocationPercentage;
//                }
//            }
//        }
//        return MathUtil.asBigDecimal(grantAmortizationRemainsStartYear);
//    }
//
//    @Override
//    public BigDecimal calculateGrantAmortizationRemainsEndYear(Vehicle vehicle, Vehicle.GrantType grantType, ContractualYear year, BigDecimal amortizationDuration,
//                                                               BigDecimal grantAmortizationRemainsStartYearPreviousYear, BigDecimal grantAmortizationPreviousYear, BigDecimal contractualNetValueStartYear) {
//        double grantAmortization = vehicle.calculateGrantAmortization(grantType, year, amortizationDuration, contractualNetValueStartYear).doubleValue();
//        double grantAmortizationRemainsStartYear = vehicle.calculateGrantAmortizationRemainsStartYear(grantType, year, grantAmortizationRemainsStartYearPreviousYear,
//                grantAmortizationPreviousYear, contractualNetValueStartYear).doubleValue();
//        return calculateGrantAmortizationRemainsEndYear(vehicle, grantType, year, grantAmortizationRemainsStartYear, grantAmortization);
//    }
//
//    private BigDecimal calculateGrantAmortizationRemainsEndYear(Vehicle vehicle, Vehicle.GrantType grantType, ContractualYear year, double grantAmortizationRemainsStartYear,
//                                                                double grantAmortization) {
//        BigDecimal grantAmortizationRemainsEndYear = BigDecimal.ZERO;
//        double allocationPercentage = MathUtil.asDoubleValue(vehicle.getAllocationPercentage(year.getNumber()));
//        grantAmortizationRemainsEndYear = MathUtil.asBigDecimal(grantAmortizationRemainsStartYear * allocationPercentage - grantAmortization);
//        return grantAmortizationRemainsEndYear;
//    }
//
//    @Override
//    public BigDecimal calculateDurationInContractualYear(Vehicle vehicle, ContractualYear year) {
//        double durationInContractualYear = 0;
//        LocalDate startDate = vehicle.getStartDate();
//        LocalDate endDate = vehicle.getEndDateEndOfMonth();
//        if (startDate != null && endDate != null) {
//            double ratio = DateUtil.days360Ratio(startDate, year.getEndDate());
//            if (ratio > 0) {
//                if (ratio <= 1) {
//                    if (endDate.isBefore(year.getEndDate())) {
//                        durationInContractualYear = DateUtil.days360Ratio(startDate, endDate);
//                    } else {
//                        durationInContractualYear = ratio;
//                    }
//                } else {
//                    if (endDate.isBefore(year.getEndDate())) {
//                        if (!endDate.isBefore(year.getStartDate())) {
//                            durationInContractualYear = 1 - DateUtil.days360Ratio(endDate, year.getEndDate());
//                        }
//                    } else {
//                        durationInContractualYear = 1;
//                    }
//                }
//            }
//        }
//        return MathUtil.asBigDecimal(durationInContractualYear);
//    }
//
//    @Override
//    public BigDecimal calculateGrantAmortization(Vehicle vehicle, Vehicle.GrantType grantType, ContractualYear year, BigDecimal amortizationDuration, BigDecimal contractualNetValueStartYear) {
//        double stifGrantAmortization = 0;
//        LocalDate startDate = vehicle.getStartDate();
//        double grant = MathUtil.asDoubleValue(vehicle.getGrant(grantType));
//        if (startDate != null) {
//            double durationInContract = vehicle.calculateDurationInContract(year).doubleValue();
//            double durationInContractualYear = vehicle.calculateDurationInContractualYear(year).doubleValue();
//            double allocationPercentage = MathUtil.asDoubleValue(vehicle.getAllocationPercentage(year.getNumber()));
//            if (startDate.isBefore(year.getEndDate()) && durationInContract < (amortizationDuration.doubleValue() + 1)) {
//                if (durationInContractualYear >= 1) {
//                    if (durationInContract <= amortizationDuration.doubleValue()) {
//                        stifGrantAmortization = grant / amortizationDuration.doubleValue() * allocationPercentage;
//                    } else {
//                        stifGrantAmortization = (1 - (durationInContract - amortizationDuration.doubleValue())) * grant / amortizationDuration.doubleValue()
//                                * allocationPercentage;
//                    }
//                } else {
//                    stifGrantAmortization = durationInContractualYear * grant / amortizationDuration.doubleValue() * allocationPercentage;
//                }
//            }
//        }
//        return MathUtil.asBigDecimal(stifGrantAmortization);
//    }
//
//    @Override
//    public BigDecimal calculateUsageRatio(Vehicle vehicle, ContractualYear year) {
//        LocalDate vehicleStartDate = vehicle.getStartDate();
//        LocalDate vehicleEndDate = vehicle.getEndDateEndOfMonth();
//        double useRatio = DateUtil.calculateUsageRatio(vehicleStartDate, vehicleEndDate, year);
//        return MathUtil.asBigDecimal(useRatio);
//    }
//
//    @Override
//    public boolean shouldUseEndYearAmortizationInsteadOfAmortizationIfLower(){
//        return true;
//    }

}
