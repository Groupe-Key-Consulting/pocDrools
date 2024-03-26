package com.kc.poc.drools.droolsConfig;

import org.kie.api.KieServices;
import org.kie.api.builder.KieScanner;
import org.kie.api.builder.model.KieBaseModel;
import org.kie.api.builder.model.KieModuleModel;
import org.kie.api.builder.model.KieSessionModel;
import org.kie.api.conf.EqualityBehaviorOption;
import org.kie.api.conf.EventProcessingOption;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.conf.ClockTypeOption;
import org.kie.util.maven.support.ReleaseIdImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.kc.poc.drools")
public class DroolsConfigTwo {
    final static String G = "com.kc.poc.drools";
    final static String A = "geode-calculation-drools";
    final static String V = "0.0.1-SNAPSHOT";
//    private static final String RULES_FILE = "rules/calculateAmortizationDuration.drl";



    @Bean
    public KieContainer kieContainer() {
        KieServices kieServices = KieServices.Factory.get();


//        KieModuleModel kieModuleModel = kieServices.newKieModuleModel();
////
////
////        //add a new kbase to the module
//        KieBaseModel kieBaseModel1 = kieModuleModel.newKieBaseModel( "KBase1")
//                .setDefault( true )
//                .setEqualsBehavior( EqualityBehaviorOption.EQUALITY )
//                .setEventProcessingMode( EventProcessingOption.STREAM );
////                .addPackage("com.kc.poc.drools.model");
////        // no packages imported means import everything
//
//        //add a new ksession to the kbase
//        KieSessionModel kieSessionModel1 = kieBaseModel1.newKieSessionModel( "KSession1" )
//                .setDefault( true )
//                .setType( KieSessionModel.KieSessionType.STATEFUL )
//                //Defines if events timestamps are determined by the system clock or by a pseudo clock controlled by the application.
//                // This clock is especially useful for unit testing temporal rules.
//                .setClockType( ClockTypeOption.get("realtime") );

//        ReleaseIdImpl releaseId = new ReleaseIdImpl(G, A, V);
        KieContainer kieContainer = kieServices.getKieClasspathContainer();
//        kieContainer = kieServices.newKieContainer(releaseId);

        KieScanner kieScanner = kieServices.newKieScanner(kieContainer);
        kieScanner.start(3000);

        return kieContainer;
    }
}
