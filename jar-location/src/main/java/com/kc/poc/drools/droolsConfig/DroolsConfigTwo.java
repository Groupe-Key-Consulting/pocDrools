package com.kc.poc.drools.droolsConfig;

import org.kie.api.KieServices;
import org.kie.api.builder.*;
import org.kie.api.runtime.KieContainer;
import org.kie.internal.io.ResourceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.kc.poc.drools")
public class DroolsConfigTwo {
    final static String G = "com.kc.poc.drools";
    final static String A = "geode-calculation-drools";
    final static String V = "0.0.1-SNAPSHOT";

    // import drl
//     private static final String RULES_FILE = "rules/calculateGrantAmortizationRemainsEndYear.drl";

    @Bean
    public KieContainer kieContainer() {
        KieServices kieServices = KieServices.Factory.get();
        ReleaseId releaseId = kieServices.newReleaseId(G, A, V);
        KieContainer kieContainer = kieServices.newKieContainer(releaseId);
        KieScanner scanner = kieServices.newKieScanner(kieContainer);

        scanner.start(3000L);
        return kieContainer;
    }

//    @Bean
//    public KieContainer kieContainerDrl() {
//        KieServices kieServices = KieServices.Factory.get();
//        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
//        kieFileSystem.write(ResourceFactory.newClassPathResource(RULES_FILE));
//        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
//        kieBuilder.buildAll();
//        KieModule kieModule = kieBuilder.getKieModule();
//        return kieServices.newKieContainer(kieModule.getReleaseId());
//    }
}
