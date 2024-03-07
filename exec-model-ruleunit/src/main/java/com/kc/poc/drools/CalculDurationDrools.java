package com.kc.poc.drools;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.drools.ruleunits.api.DataSource;
import org.drools.ruleunits.api.DataStore;
import org.drools.ruleunits.api.RuleUnitData;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
public class CalculDurationDrools implements RuleUnitData {

    private final DataStore<CalculateDurationInContractualYear> calculs;

    private final List<BigDecimal> result = new ArrayList<>();


    public CalculDurationDrools() {
        this(DataSource.createStore());
    }


    public CalculDurationDrools(DataStore<CalculateDurationInContractualYear> calcul) {
        this.calculs = calcul;
    }

    public DataStore<CalculateDurationInContractualYear> getCalculs() {
        return calculs;
    }

    public List<BigDecimal> getResult() {
        return result;
    }
}
