package com.kc.poc.drools.service;

import com.kc.poc.drools.config.DroolsConfig;
import com.kc.poc.drools.fact.Customer;
import com.kc.poc.drools.fact.Order;
import lombok.AllArgsConstructor;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.Agenda;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import static java.lang.Thread.sleep;

@Service
public class ECommerceService {


    @Qualifier("KieContainerCustom")
    private KieContainer kieContainer;

    public ECommerceService(KieContainer kieContainer) {
        this.kieContainer = kieContainer;
    }

    public String makeAnOrder(Customer customer, Order order) {
        KieSession kieSession = kieContainer.getKieBase().newKieSession();
        try {
            Agenda agenda = kieSession.getAgenda();
//            agenda.getAgendaGroup( "upgrade to silver" ).setFocus();
            agenda.getAgendaGroup( "upgrade to gold" ).setFocus();
            kieSession.insert(order);
            kieSession.insert(customer);
//            kieSession.setGlobal("customer" , customer);
            kieSession.fireAllRules();
        } finally {
            kieSession.dispose();
        }
        return customer.getCategory();
    }
}
