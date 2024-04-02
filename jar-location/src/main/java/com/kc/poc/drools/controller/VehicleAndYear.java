package com.kc.poc.drools.controller;

import com.kc.poc.drools.fact.Vehicle;
import com.kc.poc.drools.service.ContractualYear;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VehicleAndYear {
    private Vehicle vehicle;
    private ContractualYear year;
}
