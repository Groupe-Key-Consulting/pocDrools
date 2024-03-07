package com.kc.poc.drools;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
@NoArgsConstructor
public class CalculateDurationInContractualYear {

    BigDecimal durationInContractualYear = BigDecimal.ZERO;

    Vehicule vehicule;

    ContractualYear contractualYear;

    public CalculateDurationInContractualYear(BigDecimal durationInContractualYear, Vehicule vehicule, ContractualYear contractualYear) {
        super();
        this.durationInContractualYear = durationInContractualYear;
        this.vehicule = vehicule;
        this.contractualYear = contractualYear;
    }
}
