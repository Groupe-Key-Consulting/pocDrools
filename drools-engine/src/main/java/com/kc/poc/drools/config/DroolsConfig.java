package com.kc.poc.drools.config;

import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.runtime.KieContainer;
import org.kie.internal.io.ResourceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.kc.poc.drools.service")
public class DroolsConfig {

    private static final String RULES_FILE = "rules/saleRules_ExecuteAgendaGroupRules.drl";
//    private static final String RULES_FILE = "rules/saleRules_ExecuteAllValidRules.drl";
//    private static final String RULES_FILE = "rules/saleRules_ExecuteFirstValidRule.drl";

    @Bean
    public KieContainer kieContainer() {
        KieServices kieServices = KieServices.Factory.get();
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
        kieFileSystem.write(ResourceFactory.newClassPathResource(RULES_FILE));
        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
        kieBuilder.buildAll();
        KieModule kieModule = kieBuilder.getKieModule();
        return kieServices.newKieContainer(kieModule.getReleaseId());
//        KieModuleModel kieModuleModel = kieServices.newKieModuleModel();
//
//        //add a new kbase to the module
//        KieBaseModel kieBaseModel1 = kieModuleModel.newKieBaseModel("KBase1 ")
//                .setDefault(true)
//                .setEqualsBehavior(EqualityBehaviorOption.EQUALITY)
//                .setEventProcessingMode(EventProcessingOption.STREAM)
//                .addPackage(RULES_FILE);
//
//        //add a new ksession to the kbase
//        KieSessionModel ksessionModel1 = kieBaseModel1.newKieSessionModel("KSession1")
//                .setDefault(true)
//                .setType(KieSessionModel.KieSessionType.STATEFUL)
//                //Defines if events timestamps are determined by the system clock or by a pseudo clock controlled by the application.
//                // This clock is especially useful for unit testing temporal rules.
//                .setClockType(ClockTypeOption.get("realtime"));
//
//        KieFileSystem kfs = kieServices.newKieFileSystem();
//        try (Stream<Path> paths = Files.walk(Paths.get(ClassLoader.getSystemResource("").toURI()))) {
//            paths.forEach(System.out::println);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (URISyntaxException e) {
//            throw new RuntimeException(e);
//        }
//        InputStream resourceStream = getClass().getClassLoader().getResourceAsStream("rules/taxiFareRules.drl");
//        kfs.write(ResourceFactory.newInputStreamResource(resourceStream));
////        kfs.write("src/main/resources/rules/taxiFareRules.drl", RULES_FILE);
//        kfs.writeKModuleXML(kieModuleModel.toXML());
//        //Adding Kie artifacts to a KieFileSystem
//
//        //Adding a DRL file to a KieFileSystem
//        //kfs.write("src/main/resources/rules/test.drl", ResourceFactory.newClassPathResource(RULES_PATH));
//
//
//        /**
//         *  we can check if the build was successful
//         *  It is a best practice to check the compilation results.
//         *  The KieBuilder reports compilation results of 3 different severities: ERROR, WARNING and INFO
//         */
//        //KieBuilder kieBuilder = kieServices.newKieBuilder( kfs ).buildAll();
//        //assertEquals( 0, kieBuilder.getResults().getMessages( Message.Level.ERROR ).size() );
//
//        // Building the contents of a KieFileSystem and creating a KieContainer
//        kieServices.newKieBuilder(kfs).buildAll();
//        return kieServices.newKieContainer(kieServices.getRepository().getDefaultReleaseId());
    }
}
