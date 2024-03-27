package com.kc.poc.drools.droolsConfig;

import org.kie.api.KieServices;
import org.kie.api.builder.KieScanner;
import org.kie.api.builder.ReleaseId;
import org.kie.api.runtime.KieContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.kc.poc.drools")
public class DroolsConfigTwo {
    final static String G = "com.kc.poc.drools";
    final static String A = "geode-calculation-drools";
    final static String V = "0.0.1-SNAPSHOT";

    @Bean
    public KieContainer kieContainer() {
        KieServices kieServices = KieServices.Factory.get();
        ReleaseId releaseId = kieServices.newReleaseId(G, A, V);
        KieContainer kieContainer = kieServices.newKieContainer(releaseId);
        KieScanner scanner = kieServices.newKieScanner(kieContainer);

        scanner.start(3000L);
        return kieContainer;
    }
}
