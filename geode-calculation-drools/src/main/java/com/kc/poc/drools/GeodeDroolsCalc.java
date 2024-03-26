package com.kc.poc.drools;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;

@SpringBootApplication
public class GeodeDroolsCalc {
    public static void main(String[] args) {
        SpringApplication.run(GeodeDroolsCalc.class, args);

    }

    @Bean
    public KieContainer kieContainer() {
        return KieServices.Factory.get().getKieClasspathContainer();
    }

    @Bean
    public KieSession kieSession() throws IOException {
        return kieContainer().newKieSession("Session1_1");
    }

}