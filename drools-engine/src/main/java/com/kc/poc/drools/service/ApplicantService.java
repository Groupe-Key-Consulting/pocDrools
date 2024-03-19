package com.kc.poc.drools.service;

import com.kc.poc.drools.config.DroolsConfig;
import com.kc.poc.drools.model.Applicant;
import com.kc.poc.drools.model.SuggestedRole;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class ApplicantService {

    @Autowired
    @Qualifier("KieContainerCustom")
    KieContainer kieContainer;

    public ApplicantService(KieContainer kieContainer) {
        this.kieContainer = kieContainer;
    }

    public SuggestedRole suggestARoleForApplicant(Applicant applicant, SuggestedRole suggestedRole) {
        KieSession kieSession = kieContainer.getKieBase().newKieSession();
       try {
           kieSession.insert(applicant);
           kieSession.setGlobal("suggestedRole",suggestedRole);
           kieSession.fireAllRules();
       } finally {
           kieSession.dispose();
       }
       System.out.println("Suggested Role for " + applicant.getName() + " is " + suggestedRole.getRole());
       return suggestedRole;
    }
}
