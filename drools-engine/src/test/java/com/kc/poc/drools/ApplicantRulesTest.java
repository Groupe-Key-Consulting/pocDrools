package com.kc.poc.drools;

import com.kc.poc.drools.model.Applicant;
import com.kc.poc.drools.model.SuggestedRole;
import com.kc.poc.drools.service.ApplicantService;
import org.junit.jupiter.api.Test;
import org.kie.api.runtime.KieContainer;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ApplicantRulesTest {

    @Test
    public void whenCriteriaMatching_ThenSuggestManagerRole(){
        //given
        Config config = new Config();
        KieContainer kieContainer = config.createKieContainer();
        Applicant applicant = new Applicant("Niko", 37, 1600000.0,11);
        SuggestedRole suggestedRole = new SuggestedRole();
        ApplicantService applicantService = new ApplicantService(kieContainer);

        applicantService.suggestARoleForApplicant(applicant, suggestedRole);

        assertEquals("Manager", suggestedRole.getRole());
    }
}
