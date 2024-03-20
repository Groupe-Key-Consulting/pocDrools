package com.kc.poc.drools;

import org.drools.ruleunits.api.DataSource;
import org.drools.ruleunits.api.DataStore;
import org.drools.ruleunits.api.RuleUnitData;

import java.util.ArrayList;
import java.util.List;
public class CalculDurationDrools implements RuleUnitData {

    private final DataStore<CalculateDurationInContractualYear> calculs;

    private final List<String> result = new ArrayList<>();


    public CalculDurationDrools() {
        this(DataSource.createStore());
    }


    public CalculDurationDrools(DataStore<CalculateDurationInContractualYear> calculs) {
        this.calculs = calculs;
    }

    public DataStore<CalculateDurationInContractualYear> getCalculs() {
        return calculs;
    }

    public List<String> getResult() {
        return result;
    }
}
