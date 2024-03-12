package com.kc.poc.drools.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Applicant {
    private String name;
    private int age;
    private double currentSalary;
    private int experienceInYears;

    public Applicant(String name, int age, double currentSalary, int experienceInYears) {
        this.name = name;
        this.age = age;
        this.currentSalary = currentSalary;
        this.experienceInYears = experienceInYears;
    }
}
