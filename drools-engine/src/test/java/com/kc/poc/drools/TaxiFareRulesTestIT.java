package com.kc.poc.drools;

import com.kc.poc.drools.config.DroolsConfig;
import com.kc.poc.drools.model.Fare;
import com.kc.poc.drools.model.TaxiRide;
import com.kc.poc.drools.service.TaxiFareCalculatorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = DroolsRuleEngineApplication.class)
public class TaxiFareRulesTestIT {


    @Autowired
    @Qualifier("KieContainerCustom")
    KieContainer kieContainer;

    @BeforeEach
    void setUp() {

    }

    @Test
    public void whenNightSurchargeFalseAndDistLessThan10_ThenFixWithoutNightSurcharge() {
        KieSession kieSession = kieContainer.getKieBase().newKieSession();
        TaxiRide taxiRide = new TaxiRide();
        Fare fare = new Fare();
        taxiRide.setIsNightSurcharge(false);
        taxiRide.setDistanceInMile(9L);


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

        assertNotNull(fare.getTotalFare());
        assertEquals(Long.valueOf(70), fare.getTotalFare());
    }


}
