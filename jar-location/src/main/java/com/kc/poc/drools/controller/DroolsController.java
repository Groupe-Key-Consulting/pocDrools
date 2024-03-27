package com.kc.poc.drools.controller;

import com.kc.poc.drools.fact.Vehicle;
import com.kc.poc.drools.service.DroolsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/drools")
public class DroolsController {

    @Autowired
    private DroolsService droolsService;

    @PostMapping("/calculateAmortizationDuration")
    public ResponseEntity<BigDecimal> calculateAmortizationDuration(@RequestBody Vehicle vehicle) {
        BigDecimal amortizationDuration = droolsService.ScanCalculateAmortizationDurationDrools(vehicle);
        return new ResponseEntity<>(amortizationDuration, HttpStatus.OK);
    }
}
