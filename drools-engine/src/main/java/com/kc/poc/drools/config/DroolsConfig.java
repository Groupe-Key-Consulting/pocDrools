package com.kc.poc.drools.config;

import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieRepository;
import org.kie.api.builder.ReleaseId;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;

public class DroolsConfig {
    private final static String RULES_DRL = "rules/rules.drl";
    // Class KieServices = all the Kie Build abd Kie Runtime facilities
    // On va créer de nouvelles instances de Kiefilesystem, KieBuilder, KieModule et KieContainer
    private final KieServices kieServices = KieServices.Factory.get();

    //provide the container to define the Drools ressources like rules files and decision tables
    public KieFileSystem getKieFileSystem() {
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
        kieFileSystem.write(ResourceFactory.newClassPathResource(RULES_DRL));
        return kieFileSystem;
    }

    public KieSession getKieSession() {
        // build/compile les rules et check les erreurs de syntaxe
        KieBuilder kieBuilder = kieServices.newKieBuilder(getKieFileSystem());
        kieBuilder.buildAll();
        // ---
        KieRepository kieRepository = kieServices.getRepository();
        // create a new KieContainer with this KieModule using its ReleaseId.
        ReleaseId releaseId = kieRepository.getDefaultReleaseId();
        KieContainer kieContainer = kieServices.newKieContainer(releaseId);
        // We can now obtain KieSession from the KieContainer.
        // Our application interacts with the KieSession, which stores and executes on the runtime data:
        KieSession kieSession = kieContainer.newKieSession();
        return kieContainer.newKieSession();
    }
}