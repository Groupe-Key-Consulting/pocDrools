package com.kc.poc.drools.config;

import org.kie.api.KieServices;
import org.kie.api.builder.KieFileSystem;
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

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Configuration
//@ComponentScan("com.kc.poc.drools")
public class DroolsConfig {

    private static final KieServices kieServices = KieServices.Factory.get();
    private static final String RULES_PATH = "src/main/resources/rules/regles_prets.drl";

    @Bean
    public KieContainer kieContainer() {

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
                .setType( KieSessionModel.KieSessionType.STATELESS )
                //Defines if events timestamps are determined by the system clock or by a pseudo clock controlled by the application.
                // This clock is especially useful for unit testing temporal rules.
                .setClockType( ClockTypeOption.get("realtime") );


            try (Stream<Path> paths = Files.walk(Paths.get(ClassLoader.getSystemResource("").toURI()))) {
                paths
                        .forEach(System.out::println);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }

        KieFileSystem kfs = kieServices.newKieFileSystem();
        //Adding a DRL file to a KieFileSystem
        InputStream resourceStream = getClass().getClassLoader().getResourceAsStream("/rules/regles_prets.drl");
        kfs.write(ResourceFactory.newInputStreamResource(resourceStream));

        kfs.writeKModuleXML(kieModuleModel.toXML());
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
