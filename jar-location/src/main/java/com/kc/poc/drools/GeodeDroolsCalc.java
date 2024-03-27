package com.kc.poc.drools;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "com.kc.poc.drools")
@EnableScheduling
//@EnableAsync
public class GeodeDroolsCalc {
    public static void main(String[] args) {
        SpringApplication.run(GeodeDroolsCalc.class, args);
    }
}