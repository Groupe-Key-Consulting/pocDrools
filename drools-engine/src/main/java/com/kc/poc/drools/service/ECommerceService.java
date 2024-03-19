package com.kc.poc.drools.service;

import com.kc.poc.drools.config.DroolsConfig;
import com.kc.poc.drools.fact.Customer;
import com.kc.poc.drools.fact.Order;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.Agenda;
import org.springframework.stereotype.Service;

import static java.lang.Thread.sleep;

@Service
public class ECommerceService {
    KieSession kieSession = new DroolsConfig().kieContainer().newKieSession();

    public String makeAnOrder(Customer customer, Order order) {
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
