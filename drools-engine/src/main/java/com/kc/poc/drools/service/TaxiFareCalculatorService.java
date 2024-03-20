package com.kc.poc.drools.service;

import com.kc.poc.drools.model.Fare;
import com.kc.poc.drools.model.TaxiRide;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Service;

@Service
public class TaxiFareCalculatorService {


    private final KieContainer kieContainer;

    public TaxiFareCalculatorService(KieContainer kieContainer) {
        this.kieContainer = kieContainer;
    }

    public Long calculateFare(TaxiRide taxiRide, Fare fare) {
        KieSession kieSession = kieContainer.getKieBase().newKieSession();

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
