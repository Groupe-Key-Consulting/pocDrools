package com.kc.poc.drools;

import org.drools.ruleunits.api.RuleUnitInstance;
import org.drools.ruleunits.api.RuleUnitProvider;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

public class CalculateDurationContractualYearTest {

    static final Logger LOG = LoggerFactory.getLogger(CalculateDurationContractualYearTest.class);

    @Test
    void should_calcul_duration() {
        //given
        CalculDurationDrools calcul = new CalculDurationDrools();
        LOG.info("Creating CalculDurationDrools");

        LocalDate now = LocalDate.now();
        Vehicule vehicule = new Vehicule();
        vehicule.setDateDebut(now.minusYears(1));
        vehicule.setDateFin(now);

        ContractualYear contractualYear = new ContractualYear();
        contractualYear.setDateDebut(now.minusYears(2));
        contractualYear.setDateFin(now);

        CalculateDurationInContractualYear calculateDurationContractualYear = new CalculateDurationInContractualYear();
        calculateDurationContractualYear.setContractualYear(contractualYear);
        calculateDurationContractualYear.setVehicule(vehicule);
        

        RuleUnitInstance<CalculDurationDrools> instance = RuleUnitProvider.get().createRuleUnitInstance(calcul);

        try {
            LOG.info("Insert data");
            calcul.getCalculs().add(calculateDurationContractualYear);
            LOG.info("Run query. Rules are also fired");
            instance.fire();
        } finally {
            instance.close();
        }


    }
}
