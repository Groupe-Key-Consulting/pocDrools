package com.kc.poc.drools;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.kc.poc.drools")
public class DecisionTableDroolsGeode {
    public static void main(String[] args) {
        SpringApplication.run(DecisionTableDroolsGeode.class, args);
    }
}