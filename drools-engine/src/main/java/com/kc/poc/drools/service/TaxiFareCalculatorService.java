package com.kc.poc.drools.service;

import com.kc.poc.drools.config.DroolsConfig;
import com.kc.poc.drools.model.Fare;
import com.kc.poc.drools.model.TaxiRide;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaxiFareCalculatorService {

    @Autowired
    private KieContainer kieContainer;
    KieSession kieSession = new DroolsConfig().kieContainer().newKieSession();

    public Long calculateFare(TaxiRide taxiRide, Fare fare) {
        try {
            kieSession.insert(taxiRide);
            kieSession.setGlobal("fare", fare);
            kieSession.fireAllRules();
        } finally {
            kieSession.dispose();
        }
        System.out.println("|--- Test ---|");
        System.out.println("Ride distance: " + taxiRide.getDistanceInMile());
        System.out.println("Night surcharge: " + fare.getNightSurcharge());
        System.out.println("Total ride fare is: " + fare.getTotalFare());
        System.out.println("|--- END ---|\n");
        return fare.getTotalFare();
    }
}
