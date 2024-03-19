package com.kc.poc.drools;

import com.kc.poc.drools.model.Fare;
import com.kc.poc.drools.model.TaxiRide;
import com.kc.poc.drools.service.TaxiFareCalculatorService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TaxiFareRulesTest {

    @Test
    public void whenNightSurchargeFalseAndDistLessThan10_ThenFixWithoutNightSurcharge() {
        TaxiRide taxiRide = new TaxiRide();
        Fare rideFare = new Fare();
        TaxiFareCalculatorService taxiFareCalculatorService = new TaxiFareCalculatorService();
        taxiRide.setIsNightSurcharge(false);
        taxiRide.setDistanceInMile(9L);
        Long totalCharge;
        totalCharge = taxiFareCalculatorService.calculateFare(taxiRide, rideFare);

        assertNotNull(totalCharge);
        assertEquals(Long.valueOf(70), totalCharge);
    }

    @Test
    public void whenNightSurchargeRueAndDistBetween10And20_ThenFixWithNightSurcharge() {
        TaxiRide taxiRide = new TaxiRide();
        Fare rideFare = new Fare();
        TaxiFareCalculatorService taxiFareCalculatorService = new TaxiFareCalculatorService();
        taxiRide.setIsNightSurcharge(true);
        taxiRide.setDistanceInMile(18L);
        Long totalCharge;
        totalCharge = taxiFareCalculatorService.calculateFare(taxiRide, rideFare);

        assertNotNull(totalCharge);
        assertEquals(Long.valueOf(105), totalCharge);
    }

    @Test
    public void whenNightSurchargeFalseAndDistBetween20And40_ThenFixWithoutNightSurcharge() {
        TaxiRide taxiRide = new TaxiRide();
        Fare rideFare = new Fare();
        TaxiFareCalculatorService taxiFareCalculatorService = new TaxiFareCalculatorService();
        taxiRide.setIsNightSurcharge(false);
        taxiRide.setDistanceInMile(32L);
        Long totalCharge;
        totalCharge = taxiFareCalculatorService.calculateFare(taxiRide, rideFare);

        assertNotNull(totalCharge);
        assertEquals(Long.valueOf(120), totalCharge);
    }
}
