package com.kc.poc.drools.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.kc.poc.drools.fact.Vehicle;
import com.kc.poc.drools.service.ContractualYear;
import com.kc.poc.drools.service.VehicleStrategy2021Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/drools")
public class DroolsController {

    @Autowired
    private VehicleStrategy2021Service droolsService;

    @PostMapping("/calculateAmortizationDuration")
    public ResponseEntity<BigDecimal> calculateAmortizationDuration(@RequestBody Vehicle vehicle) {
        BigDecimal amortizationDuration = droolsService.ScanCalculateAmortizationDurationDrools(vehicle);
        return new ResponseEntity<>(amortizationDuration, HttpStatus.OK);
    }

    @PostMapping("/calculateAmortizationDurationStateless")
    public ResponseEntity<BigDecimal> calculateAmortizationDurationStatelessList(@RequestBody Vehicle vehicle) {
        BigDecimal amortizationDuration = droolsService.StatelessScanCalculateAmortizationDurationDrools(vehicle);
        return new ResponseEntity<>(amortizationDuration, HttpStatus.OK);
    }

    @PostMapping("/calculateGrantAmortizationRemainsStartYearDrools")
    public ResponseEntity<BigDecimal> calculateGrantAmortizationRemainsStartYear(@RequestBody VehicleAndYear requestBody) {
        Vehicle vehicle = requestBody.getVehicle();
        ContractualYear year = requestBody.getYear();
        BigDecimal grantAmortizationRemainsStartYear = droolsService.statefulScanCalculateGrantAmortizationRemainsStartYearDrools(vehicle, Vehicle.GrantType.STIF, year, BigDecimal.valueOf(5), BigDecimal.valueOf(500.0), BigDecimal.valueOf(300.0));
        return new ResponseEntity<>(grantAmortizationRemainsStartYear, HttpStatus.OK);
    }

}
