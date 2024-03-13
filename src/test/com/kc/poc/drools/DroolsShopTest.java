package com.kc.poc.drools;

import com.kc.poc.drools.DroolsApplication;
import com.kc.poc.drools.model.Participant;
import com.kc.poc.drools.model.Taux;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = DroolsApplication.class)
public class DroolsShopTest {

    @Autowired
    KieContainer kieContainer;

    @BeforeEach
    void setUp() {

    }

    @Test
    public void testDiscount() {
        KieSession kieSession = kieContainer.getKieBase().newKieSession();
        Taux taux = new Taux();
        kieSession.setGlobal("taux", new Taux());
        kieSession.insert(new Participant("John", 20, 550, 90000, 10000, 4000));
        kieSession.fireAllRules();
        kieSession.dispose();

        assertThat(taux.getTauxDePret()).isEqualTo(7.25);
        assertThat(taux.getEtatDuPret()).isEqualTo("Approved");

    }
}
