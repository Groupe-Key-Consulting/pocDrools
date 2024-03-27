package com.kc.poc.drools.service;

import com.kc.poc.drools.fact.Vehicle;
import com.kc.poc.drools.util.MathUtil;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class DroolsService {

    @Autowired
    private KieContainer kieContainer;

    public BigDecimal ScanCalculateAmortizationDurationDrools(Vehicle vehicle) {
        KieSession kieSession = kieContainer.newKieSession("GeodeDroolsCalcSession");

        try {
            kieSession.insert(vehicle);
            kieSession.fireAllRules();
        } finally {
            kieSession.dispose();
        }
        return MathUtil.asBigDecimal(vehicle.getAmortizationDuration());
    }
}
