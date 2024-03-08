package com.kc.poc.drools;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.drools.ruleunits.api.RuleUnitData;
@Getter
@Setter
@EqualsAndHashCode
public class Test {

    private String name;
    private int age;
    private Vehicule vehicule;
    private ContractualYear contractualYear;

    public Test(String name, int age, Vehicule vehicule) {
        this.name = name;
        this.age = age;
        this.vehicule = vehicule;
    }

    public Test() {
    }
}
