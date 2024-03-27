package com.kc.poc.drools.strategies;


import com.kc.poc.drools.droolsConfig.DroolsConfigTwo;
import com.kc.poc.drools.fact.Vehicle;
import com.kc.poc.drools.util.MathUtil;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.scheduling.annotation.Scheduled;

import java.math.BigDecimal;
import java.time.LocalDate;

//@Service
//@Async
public class VehicleStrategy2021Two {

    public BigDecimal calculateAmortizationDurationDrools(Vehicle vehicle) {
        KieContainer kieContainer = new DroolsConfigTwo().kieContainer();
        KieSession kieSession = kieContainer.newKieSession("GeodeDroolsCalcSession");

        try {
            kieSession.insert(vehicle);
            int fired = kieSession.fireAllRules();
            System.out.println("Nombre de règles déclenchées: " + fired);
        } finally {
            kieSession.dispose();
        }
        return MathUtil.asBigDecimal(vehicle.getAmortizationDuration());
    }

    @Scheduled(fixedRate = 8000)
    public void scheduledScanCalculateAmortizationDurationDrools() {
        LocalDate date = LocalDate.now();
        Vehicle vehicle = new Vehicle();
        vehicle.setNewVehicle(true);
        vehicle.setPurchaseDate(date.minusYears(2));
        vehicle.setStartDate(date.minusYears(1));
        vehicle.setNewVehiclesAmortizationPeriod(5);
        vehicle.setOldVehiclesAmortizationPeriod(0);

        KieContainer kieContainer = new DroolsConfigTwo().kieContainer();
        KieSession kieSession = kieContainer.newKieSession("GeodeDroolsCalcSession");

        System.out.println("---- Old AmortizationDuration is: " + vehicle.getAmortizationDuration());
        try {
            kieSession.insert(vehicle);
            System.out.println("[fireAllRules]");
            kieSession.fireAllRules();
        } finally {
            kieSession.dispose();
        }
        System.out.println("---- New AmortizationDuration is: " + vehicle.getAmortizationDuration() + "\n----------------------");

    }
}