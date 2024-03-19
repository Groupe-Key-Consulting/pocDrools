package com.kc.poc.drools.config;

import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.builder.model.KieBaseModel;
import org.kie.api.builder.model.KieModuleModel;
import org.kie.api.builder.model.KieSessionModel;
import org.kie.api.conf.EqualityBehaviorOption;
import org.kie.api.conf.EventProcessingOption;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.conf.ClockTypeOption;
import org.kie.internal.io.ResourceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Configuration
@ComponentScan("com.kc.poc.drools")
public class DroolsConfig {

    private static final String RULES_FILE_AGENDA = "rules/saleRules_ExecuteAgendaGroupRules.drl";
    private static final String RULES_FILE_ALL = "rules/saleRules_ExecuteAllValidRules.drl";
    private static final String RULES_FILE_FIRST = "rules/saleRules_ExecuteFirstValidRule.drl";

    public static final String RULES_PATH = "rules/rules.drl";

    public static final String RULES_TAXI_FARE = "rules/taxiFareRules.drl";

    @Primary
    @Bean()
    public KieContainer kieContainerBasic() {
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


    @Bean("KieContainerCustom")
    public KieContainer kieContainerCustom() {

        KieServices kieServices = KieServices.Factory.get();

        KieModuleModel kieModuleModel = kieServices.newKieModuleModel();


        //add a new kbase to the module
        KieBaseModel kieBaseModel1 = kieModuleModel.newKieBaseModel( "KBase1")
                .setDefault( true )
                .setEqualsBehavior( EqualityBehaviorOption.EQUALITY )
                .setEventProcessingMode( EventProcessingOption.STREAM );
//                .addPackage("com.kc.poc.drools.model");
        // no packages imported means import everything


        //add a new ksession to the kbase
        KieSessionModel ksessionModel1 = kieBaseModel1.newKieSessionModel( "KSession1" )
                .setDefault( true )
                .setType( KieSessionModel.KieSessionType.STATEFUL )
                //Defines if events timestamps are determined by the system clock or by a pseudo clock controlled by the application.
                // This clock is especially useful for unit testing temporal rules.
                .setClockType( ClockTypeOption.get("realtime") );

        KieFileSystem kfs = kieServices.newKieFileSystem();

        kfs.writeKModuleXML(kieModuleModel.toXML());

        //Adding a DRL file to a KieFileSystem
        kfs.write(kieServices.getResources().newClassPathResource(RULES_PATH));
        kfs.write(kieServices.getResources().newClassPathResource(RULES_TAXI_FARE));
        kfs.write(kieServices.getResources().newClassPathResource(RULES_FILE_AGENDA));
        kfs.write(kieServices.getResources().newClassPathResource(RULES_FILE_ALL));
        kfs.write(kieServices.getResources().newClassPathResource(RULES_FILE_FIRST));
        //Adding Kie artifacts to a KieFileSystem

        //kfs.write("src/main/resources/rules/test.drl", ResourceFactory.newClassPathResource(RULES_PATH));


        /**
         *  we can check if the build was successful
         *  It is a best practice to check the compilation results.
         *  The KieBuilder reports compilation results of 3 different severities: ERROR, WARNING and INFO
         */
        //KieBuilder kieBuilder = kieServices.newKieBuilder( kfs ).buildAll();
        //assertEquals( 0, kieBuilder.getResults().getMessages( Message.Level.ERROR ).size() );

        // Building the contents of a KieFileSystem and creating a KieContainer
        kieServices.newKieBuilder( kfs ).buildAll();
        return kieServices.newKieContainer(kieServices.getRepository().getDefaultReleaseId());

    }

}
