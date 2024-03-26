package com.kc.poc.drools;

import org.kie.api.KieServices;
import org.kie.api.builder.*;
import org.kie.api.runtime.KieContainer;
import org.kie.internal.io.ResourceFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;

public class GeodeDroolsCalc {
    public static void main(String[] args) {
        SpringApplication.run(GeodeDroolsCalc.class, args);
    }
}