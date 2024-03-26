package com.kc.poc.drools;

import org.kie.api.KieServices;
import org.kie.api.builder.*;
import org.kie.api.runtime.KieContainer;
import org.kie.internal.io.ResourceFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "com.kc.poc.drools")
@EnableScheduling
@EnableAsync
public class GeodeDroolsCalc {
    public static void main(String[] args) {
        SpringApplication.run(GeodeDroolsCalc.class, args);
    }
}