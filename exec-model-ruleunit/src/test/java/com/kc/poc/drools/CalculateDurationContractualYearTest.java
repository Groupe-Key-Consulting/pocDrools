package com.kc.poc.drools;

import org.drools.ruleunits.api.RuleUnitInstance;
import org.drools.ruleunits.api.RuleUnitProvider;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class CalculateDurationContractualYearTest {

    static final Logger LOG = LoggerFactory.getLogger(CalculateDurationContractualYearTest.class);

    @Test
    @Disabled
    void should_calcul_duration() {
        //given
        TestUnit testUnit= new TestUnit();
        LOG.info("Creating TestUnit");

        TestDrools test = new TestDrools();
        Vehicule vehicule = new Vehicule();
        vehicule.setContractualNetValueStartYear(BigDecimal.valueOf(1));
        vehicule.setGrantType(Vehicule.GrantType.STIF);
        test.setVehicule(vehicule);



        RuleUnitInstance<TestUnit> instance = RuleUnitProvider.get().createRuleUnitInstance(testUnit);

        try {
            LOG.info("Insert data");
            testUnit.getTests().add(test);
            LOG.info("Run query. Rules are also fired");
            List<TestDrools> queryResult = instance.executeQuery("calculAmortissement").toList("$m");
            List<BigDecimal> result = instance.ruleUnitData().getResult();
            assertThat(queryResult).isNotEmpty();
            assertThat(queryResult.getFirst().getGrantAmortizationRemainsStartYear()).isEqualTo(BigDecimal.valueOf(20));
            assertThat(result).isNotEmpty();



        } finally {
            instance.close();
        }


    }
}
