package com.kc.poc.drools;

import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.runtime.KieContainer;
import org.kie.internal.io.ResourceFactory;

public class Config {

    private static final String RULES_FILE_AGENDA = "rules/saleRules_ExecuteAgendaGroupRules.drl";
    private static final String RULES_FILE_ALL = "rules/saleRules_ExecuteAllValidRules.drl";
    private static final String RULES_FILE_FIRST = "rules/saleRules_ExecuteFirstValidRule.drl";

    public static final String RULES_PATH = "rules/rules.drl";

    public static final String RULES_TAXI_FARE = "rules/taxiFareRules.drl";

    public KieContainer createKieContainer() {
        KieServices kieServices = KieServices.Factory.get();
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
        kieFileSystem.write(ResourceFactory.newClassPathResource(RULES_FILE_AGENDA));
        kieFileSystem.write(ResourceFactory.newClassPathResource(RULES_FILE_ALL));
        kieFileSystem.write(ResourceFactory.newClassPathResource(RULES_FILE_FIRST));
        kieFileSystem.write(ResourceFactory.newClassPathResource(RULES_PATH));
        kieFileSystem.write(ResourceFactory.newClassPathResource(RULES_TAXI_FARE));

        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
        kieBuilder.buildAll();
        KieModule kieModule = kieBuilder.getKieModule();
        return kieServices.newKieContainer(kieModule.getReleaseId());
    }
}
