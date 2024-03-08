package com.kc.poc.drools;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@EqualsAndHashCode
public class TestDrools {

    private String name;
    private int age;
    private Vehicule vehicule;
    private BigDecimal grantAmortizationRemainsStartYear;

    public TestDrools(String name, int age, Vehicule vehicule, BigDecimal grantAmortizationRemainsStartYear) {
        this.name = name;
        this.age = age;
        this.vehicule = vehicule;
        this.grantAmortizationRemainsStartYear = grantAmortizationRemainsStartYear;
    }

    public TestDrools() {
    }
}
