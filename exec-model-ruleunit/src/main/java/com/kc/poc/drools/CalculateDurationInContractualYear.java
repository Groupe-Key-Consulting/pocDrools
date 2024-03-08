package com.kc.poc.drools;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.kie.api.definition.type.Position;
import org.kie.api.definition.type.PropertyReactive;

import java.math.BigDecimal;
@Getter
@Setter
@NoArgsConstructor
@PropertyReactive
@EqualsAndHashCode
public class CalculateDurationInContractualYear {

    @Position(0)
    BigDecimal durationInContractualYear = BigDecimal.ZERO;
    @Position(1)
    Vehicule vehicule;
    @Position(2)
    ContractualYear contractualYear;

    public CalculateDurationInContractualYear(BigDecimal durationInContractualYear, Vehicule vehicule, ContractualYear contractualYear) {
        this.durationInContractualYear = durationInContractualYear;
        this.vehicule = vehicule;
        this.contractualYear = contractualYear;
    }


}
