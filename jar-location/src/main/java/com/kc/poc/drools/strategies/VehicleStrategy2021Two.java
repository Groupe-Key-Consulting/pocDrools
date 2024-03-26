package com.kc.poc.drools.strategies;


import com.kc.poc.drools.droolsConfig.DroolsConfigTwo;
import com.kc.poc.drools.fact.Vehicle;
import com.kc.poc.drools.util.MathUtil;
import org.kie.api.KieBase;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class VehicleStrategy2021Two {

    public BigDecimal calculateAmortizationDurationDrools(Vehicle vehicle) {
//        KieSession kieSession = new DroolsConfigTwo().kieContainer().getKieBase("KBase1").newKieSession();
        KieContainer kieContainer = new DroolsConfigTwo().kieContainer();
        KieBase kieBase = kieContainer.getKieBase("KBase1");
        KieSession kieSession = kieContainer.newKieSession("KSession1");

        try {
            kieSession.insert(vehicle);
            int fired =  kieSession.fireAllRules();
            System.out.println("Nombre de règles déclenchées: " + fired);
        } finally {
            kieSession.dispose();
        }
        return MathUtil.asBigDecimal(vehicle.getAmortizationDuration());
    }
}
