package com.kc.poc.drools.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaxiRide {
    private Boolean isNightSurcharge;
    private Long distanceInMile;
}
