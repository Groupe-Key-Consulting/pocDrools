package com.kc.poc.drools;

import org.drools.ruleunits.api.DataSource;
import org.drools.ruleunits.api.DataStore;
import org.drools.ruleunits.api.RuleUnitData;
import org.drools.ruleunits.api.SingletonStore;

public class TestUnit implements RuleUnitData {

    private final DataStore<Test> tests;
    private final SingletonStore<String> result;

    public TestUnit() {
        this(DataSource.createStore(), DataSource.createSingleton());
    }

    public TestUnit(DataStore<Test> tests, SingletonStore<String> result) {
        this.tests = tests;
        this.result = result;
    }

    public DataStore<Test> getTests() {
        return tests;
    }

    public SingletonStore<String> getResult() {
        return result;
    }
}
