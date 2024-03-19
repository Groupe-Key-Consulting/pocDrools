package com.kc.poc.drools.service;

import com.kc.poc.drools.config.DroolsConfig;
import com.kc.poc.drools.model.Applicant;
import com.kc.poc.drools.model.SuggestedRole;
import org.kie.api.runtime.KieSession;

public class ApplicantService {
    KieSession kieSession = new DroolsConfig().getKieSession();

    public SuggestedRole suggestARoleForApplicant(Applicant applicant, SuggestedRole suggestedRole) {
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
