package com.kc.poc.drools.strategies;


import com.kc.poc.drools.droolsConfig.DroolsConfig;
import com.kc.poc.drools.fact.Vehicle;
import com.kc.poc.drools.strategies.IVehicleStrategy;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class VehicleStrategy2021 implements IVehicleStrategy {

    KieSession kieSession = new DroolsConfig().kieContainer().newKieSession();

    public BigDecimal calculateAmortizationDuration(Vehicle vehicle) {
        double amortizationDuration = 0;
        if (vehicle.getType() != null && vehicle.isNewVehicle() && vehicle.getType().getNewVehiclesAmortizationPeriod() != null) {
            amortizationDuration = vehicle.getType().getNewVehiclesAmortizationPeriod();
        } else {
            if ( vehicle.getType() != null && vehicle.getPurchaseDate() != null && vehicle.getStartDate() != null && vehicle.getType().getOldVehiclesAmortizationPeriod() != null) {
                if ( vehicle.getPurchaseDate().plusMonths(vehicle.getType().getOldVehiclesAmortizationPeriod() * 12).isAfter(vehicle.getStartDate())) {
                    amortizationDuration = vehicle.getType().getOldVehiclesAmortizationPeriod() - ((double) DateUtil.dateDifMonths(vehicle.getPurchaseDate(), vehicle.getStartDate()) / 12);
                }
            }
        }
        if (amortizationDuration < 0) {
            amortizationDuration = 0;
        }
        return MathUtil.asBigDecimal(amortizationDuration);
    }
}
