package com.kc.poc.drools.droolsConfig;

import org.drools.compiler.kie.builder.impl.InternalKieModule;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieScanner;
import org.kie.api.builder.ReleaseId;
import org.kie.api.builder.model.KieBaseModel;
import org.kie.api.builder.model.KieModuleModel;
import org.kie.api.builder.model.KieSessionModel;
import org.kie.api.conf.EqualityBehaviorOption;
import org.kie.api.conf.EventProcessingOption;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.conf.ClockTypeOption;
import org.kie.scanner.KieMavenRepository;
import org.kie.util.maven.support.ReleaseIdImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableScheduling;

import static org.kie.scanner.KieMavenRepository.getKieMavenRepository;

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
//    private static final String RULES_FILE = "rules/calculateAmortizationDuration.drl";

//    @Bean
//    public KieContainer kieContainer() {
//        KieServices kieServices = KieServices.Factory.get();
//        ReleaseId releaseId = kieServices.newReleaseId(G, A, V);
//
//        KieModuleModel module = kieServices.newKieModuleModel();
//        KieBaseModel baseModel = module.newKieBaseModel("GeodeDroolsCalcKbase")
//                .setDefault(true)
//                .addPackage(G);
//        baseModel.newKieSessionModel("GeodeDroolsCalcSession").setDefault(true);
//
//        KieFileSystem kfs = kieServices.newKieFileSystem();
//        kfs.writeKModuleXML(module.toXML());
//        kfs.generateAndWritePomXML(releaseId);
//        kieServices.newKieBuilder(kfs).buildAll();
//
////        KieContainer kieContainer = kieServices.getKieClasspathContainer();
//
//        KieContainer kieContainerScan = kieServices.newKieContainer(releaseId);
//        KieScanner kieScanner = kieServices.newKieScanner(kieContainerScan);
//        kieScanner.start(3000);
//
//        return kieContainerScan;
//    }

}
