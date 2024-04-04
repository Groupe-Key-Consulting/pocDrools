package com.kc.poc.drools.service;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class VehicleYearData {
    private Integer year;
    private BigDecimal allocationPercentage = BigDecimal.ZERO;
}
