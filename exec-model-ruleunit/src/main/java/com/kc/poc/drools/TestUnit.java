package com.kc.poc.drools;

import org.drools.ruleunits.api.DataSource;
import org.drools.ruleunits.api.DataStore;
import org.drools.ruleunits.api.RuleUnitData;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TestUnit implements RuleUnitData {

    private final DataStore<TestDrools> tests;
    private List<BigDecimal> result = new ArrayList<>();

    public TestUnit() {
        this(DataSource.createStore());
    }

    public TestUnit(DataStore<TestDrools> tests) {
        this.tests = tests;
    }

    public DataStore<TestDrools> getTests() {
        return tests;
    }

    public List<BigDecimal> getResult() {
        return result;
    }
}
