package com.kc.poc.drools.strategies;


import com.kc.poc.drools.droolsConfig.DroolsConfig;
import com.kc.poc.drools.fact.Vehicle;
import com.kc.poc.drools.util.DateUtil;
import com.kc.poc.drools.util.MathUtil;
import org.drools.core.event.DefaultAgendaEventListener;
import org.kie.api.event.rule.AfterMatchFiredEvent;
import org.kie.api.event.rule.BeforeMatchFiredEvent;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class VehicleStrategy2021 {



    public BigDecimal calculateAmortizationDurationJava(Vehicle vehicle) {
        double amortizationDuration = 0;
        if (vehicle.isNewVehicle() && vehicle.getNewVehiclesAmortizationPeriod()> 0) {
            amortizationDuration = vehicle.getNewVehiclesAmortizationPeriod();
        } else {
            if (vehicle.getPurchaseDate() != null && vehicle.getStartDate() != null && vehicle.getOldVehiclesAmortizationPeriod() < 0) {
                if ( vehicle.getPurchaseDate().plusMonths( vehicle.getOldVehiclesAmortizationPeriod()* 12).isAfter(vehicle.getStartDate())) {
                    amortizationDuration = vehicle.getOldVehiclesAmortizationPeriod() - ((double) DateUtil.dateDifMonths(vehicle.getPurchaseDate(), vehicle.getStartDate()) / 12);
                }
            }
        }
        if (amortizationDuration < 0) {
            amortizationDuration = 0;
        }
        return MathUtil.asBigDecimal(amortizationDuration);
    }

    public BigDecimal calculateAmortizationDurationDrools(Vehicle vehicle) {
        KieSession kieSession = new DroolsConfig().kieContainer().newKieSession();

//        kieSession.addEventListener( new DebugAgendaEventListener() );
        kieSession.addEventListener(new DefaultAgendaEventListener() {
            public void afterMatchFired(AfterMatchFiredEvent event) {
                super.afterMatchFired( event );
                System.out.println( event );
            }
            public void beforeMatchFired(BeforeMatchFiredEvent event) {
                super.beforeMatchFired( event );
                System.out.println( event );
            }
        });
        try {

            kieSession.insert(vehicle);
            kieSession.fireAllRules();

        } finally {
            kieSession.dispose();
        }

        return MathUtil.asBigDecimal(vehicle.getAmortizationDuration());

    }
}
