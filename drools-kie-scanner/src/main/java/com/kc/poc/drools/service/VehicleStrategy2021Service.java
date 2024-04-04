package com.kc.poc.drools.service;

import com.kc.poc.drools.droolsConfig.DroolsConfigTwo;
import com.kc.poc.drools.fact.Vehicle;
import com.kc.poc.drools.strategies.IVehicleStrategy;
import com.kc.poc.drools.util.DateUtil;
import com.kc.poc.drools.util.MathUtil;
import org.kie.api.command.Command;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.StatelessKieSession;
import org.kie.internal.command.CommandFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class VehicleStrategy2021Service implements IVehicleStrategy {

    @Autowired
    private KieContainer kieContainer;

    public BigDecimal ScanCalculateAmortizationDurationDrools(Vehicle vehicle) {
        KieSession kieSession = kieContainer.newKieSession("GeodeDroolsCalcSession");

        try {
            kieSession.insert(vehicle);
            kieSession.fireAllRules();
        } finally {
            kieSession.dispose();
        }
        return MathUtil.asBigDecimal(vehicle.getAmortizationDuration());
    }

    public BigDecimal StatelessScanCalculateAmortizationDurationDrools(Vehicle vehicle) {
        StatelessKieSession kieSession = kieContainer.newStatelessKieSession("GeodeDroolsCalcSessionStateless");

        kieSession.execute(vehicle);
        return MathUtil.asBigDecimal(vehicle.getAmortizationDuration());
    }

    public BigDecimal calculateAmortizationDurationDrools(Vehicle vehicle) {
        KieContainer kieContainer = new DroolsConfigTwo().kieContainer();
        KieSession kieSession = kieContainer.newKieSession("GeodeDroolsCalcSession");

        try {
            kieSession.insert(vehicle);
            int fired = kieSession.fireAllRules();
            System.out.println("Nombre de règles déclenchées: " + fired);
        } finally {
            kieSession.dispose();
        }
        return MathUtil.asBigDecimal(vehicle.getAmortizationDuration());
    }

    @Scheduled(fixedRate = 8000)
    public void scheduledScanCalculateAmortizationDurationDrools() {
        KieSession kieSession = kieContainer.newKieSession("GeodeDroolsCalcSession");
        LocalDate date = LocalDate.now();
        Vehicle vehicle = new Vehicle();
        vehicle.setNewVehicle(true);
        vehicle.setPurchaseDate(date.minusYears(2));
        vehicle.setStartDate(date.minusYears(1));
        vehicle.setNewVehiclesAmortizationPeriod(5);
        vehicle.setOldVehiclesAmortizationPeriod(0);

        try {
            kieSession.insert(vehicle);
            kieSession.fireAllRules();
        } finally {
            kieSession.dispose();
        }
    }

    public long calculationTimeOfAmortizationDurationStateless(List<Command> cmd) {
        KieContainer kieContainer = new DroolsConfigTwo().kieContainer();
        StatelessKieSession kieSession = kieContainer.newStatelessKieSession("GeodeDroolsCalcSessionStateless");

        LocalDateTime startDate = LocalDateTime.now();
        kieSession.execute(CommandFactory.newBatchExecution(cmd));
        LocalDateTime endDate = LocalDateTime.now();
        return Duration.between(startDate, endDate).toMillis();
    }

    public long calculationTimeOfAmortizationDurationMultipleStateless(List<Command> cmd) {
        KieContainer kieContainer = new DroolsConfigTwo().kieContainer();
        StatelessKieSession kieSession0 = kieContainer.newStatelessKieSession("GeodeDroolsCalcSessionStateless0");
        StatelessKieSession kieSession1 = kieContainer.newStatelessKieSession("GeodeDroolsCalcSessionStateless1");
        StatelessKieSession kieSession2 = kieContainer.newStatelessKieSession("GeodeDroolsCalcSessionStateless2");
        StatelessKieSession kieSession3 = kieContainer.newStatelessKieSession("GeodeDroolsCalcSessionStateless3");
        StatelessKieSession kieSession4 = kieContainer.newStatelessKieSession("GeodeDroolsCalcSessionStateless4");

        LocalDateTime startDate = LocalDateTime.now();
        kieSession0.execute(CommandFactory.newBatchExecution(cmd));
        kieSession1.execute(CommandFactory.newBatchExecution(cmd));
        kieSession2.execute(CommandFactory.newBatchExecution(cmd));
        kieSession3.execute(CommandFactory.newBatchExecution(cmd));
        kieSession4.execute(CommandFactory.newBatchExecution(cmd));
        LocalDateTime endDate = LocalDateTime.now();
        return Duration.between(startDate, endDate).toMillis();
    }

    public long calculationTimeOfAmortizationDurationStateful_ImportedObjects(List<Command> cmd) {
        KieContainer kieContainer = new DroolsConfigTwo().kieContainer();
        KieSession kieSession = kieContainer.newKieSession("GeodeDroolsCalcSession");

        LocalDateTime startDate = LocalDateTime.now();
        try {
            for (Command command : cmd) {
                kieSession.insert(command);
            }
            kieSession.fireAllRules();
        } finally {
            kieSession.dispose();
        }
        LocalDateTime endDate = LocalDateTime.now();
        return Duration.between(startDate, endDate).toMillis();
    }

    public long calculationTimeOfAmortizationDurationStateful_CreateObjectsHere() {
        KieContainer kieContainer = new DroolsConfigTwo().kieContainer();
        KieSession kieSession = kieContainer.newKieSession("GeodeDroolsCalcSession");

        LocalDateTime startDate = LocalDateTime.now();
        try {
            LocalDate now = LocalDate.now();

            for (int i = 0; i < 1000000; i++) {
                boolean oldVehicle = new Random().nextBoolean();
                kieSession.insert(new Vehicle(
                        now.minusYears(2).minusMonths(new Random().nextInt(12)),
                        oldVehicle,
                        now.minusYears(1).minusMonths(new Random().nextInt(12)),
                        oldVehicle ? 0 : new Random().nextInt(5),
                        oldVehicle ? new Random().nextInt(5) : 0,
                        0,
                        null,
                        null
                ));
            }
            kieSession.fireAllRules();
        } finally {
            kieSession.dispose();
        }
        LocalDateTime endDate = LocalDateTime.now();
        return Duration.between(startDate, endDate).toMillis();
    }

    public BigDecimal calculationTimeOfAmortizationDurationJava(Vehicle vehicle) {
        double amortizationDuration = 0;
        if (vehicle.isNewVehicle() && vehicle.getNewVehiclesAmortizationPeriod() > 0) {
            amortizationDuration = vehicle.getNewVehiclesAmortizationPeriod();
        } else {
            if (vehicle.getPurchaseDate() != null && vehicle.getStartDate() != null && vehicle.getOldVehiclesAmortizationPeriod() < 0) {
                if (vehicle.getPurchaseDate().plusMonths(vehicle.getOldVehiclesAmortizationPeriod() * 12).isAfter(vehicle.getStartDate())) {
                    amortizationDuration = vehicle.getOldVehiclesAmortizationPeriod() - ((double) DateUtil.dateDifMonths(vehicle.getPurchaseDate(), vehicle.getStartDate()) / 12);
                }
            }
        }
        if (amortizationDuration < 0) {
            amortizationDuration = 0;
        }
        return MathUtil.asBigDecimal(amortizationDuration);
    }

    public long calculationTimeOfAmortizationDurationJava_ImportedObjects(List<Vehicle> vehicles) {
        LocalDateTime startDate = LocalDateTime.now();
        for (Vehicle vehicle : vehicles) {
            double amortizationDuration = 0;
            if (vehicle.isNewVehicle() && vehicle.getNewVehiclesAmortizationPeriod() > 0) {
                amortizationDuration = vehicle.getNewVehiclesAmortizationPeriod();
            } else {
                if (vehicle.getPurchaseDate() != null && vehicle.getStartDate() != null && vehicle.getOldVehiclesAmortizationPeriod() < 0) {
                    if (vehicle.getPurchaseDate().plusMonths(vehicle.getOldVehiclesAmortizationPeriod() * 12).isAfter(vehicle.getStartDate())) {
                        amortizationDuration = vehicle.getOldVehiclesAmortizationPeriod() - ((double) DateUtil.dateDifMonths(vehicle.getPurchaseDate(), vehicle.getStartDate()) / 12);
                    }
                }
            }
            if (amortizationDuration < 0) {
                amortizationDuration = 0;
            }
        }
        LocalDateTime endDate = LocalDateTime.now();
        return Duration.between(startDate, endDate).toMillis();
    }

    public long calculationTimeOfAmortizationDurationJava_CreateObjectsHere() {
        LocalDateTime startDate = LocalDateTime.now();
        for (int i = 0; i < 1000000; i++) {
            Vehicle vehicle = new Vehicle();
            double amortizationDuration = 0;
            if (vehicle.isNewVehicle() && vehicle.getNewVehiclesAmortizationPeriod() > 0) {
                amortizationDuration = vehicle.getNewVehiclesAmortizationPeriod();
            } else {
                if (vehicle.getPurchaseDate() != null && vehicle.getStartDate() != null && vehicle.getOldVehiclesAmortizationPeriod() < 0) {
                    if (vehicle.getPurchaseDate().plusMonths(vehicle.getOldVehiclesAmortizationPeriod() * 12).isAfter(vehicle.getStartDate())) {
                        amortizationDuration = vehicle.getOldVehiclesAmortizationPeriod() - ((double) DateUtil.dateDifMonths(vehicle.getPurchaseDate(), vehicle.getStartDate()) / 12);
                    }
                }
            }
            if (amortizationDuration < 0) {
                amortizationDuration = 0;
            }
        }
        LocalDateTime endDate = LocalDateTime.now();
        return Duration.between(startDate, endDate).toMillis();
    }

    public BigDecimal calculateGrantAmortizationRemainsEndYearJava(Vehicle vehicle, Vehicle.GrantType grantType, ContractualYear year, double grantAmortizationRemainsStartYear, double grantAmortization) {
        BigDecimal grantAmortizationRemainsEndYear = BigDecimal.ZERO;
        double allocationPercentage = MathUtil.asDoubleValue(vehicle.getAllocationPercentage(year.getNumber()));
        grantAmortizationRemainsEndYear = MathUtil.asBigDecimal(grantAmortizationRemainsStartYear * allocationPercentage - grantAmortization);
        return grantAmortizationRemainsEndYear;
    }

    @Override
    public BigDecimal calculateGrantAmortizationRemainsEndYearJava(Vehicle vehicle, Vehicle.GrantType grantType, ContractualYear year, BigDecimal amortizationDuration, BigDecimal grantAmortizationRemainsStartYearPreviousYear, BigDecimal grantAmortizationPreviousYear, BigDecimal contractualNetValueStartYear) {
        double grantAmortization = vehicle.calculateGrantAmortization(grantType, year, amortizationDuration, contractualNetValueStartYear).doubleValue();
        double grantAmortizationRemainsStartYear = vehicle.calculateGrantAmortizationRemainsStartYear(grantType, year, grantAmortizationRemainsStartYearPreviousYear,
                grantAmortizationPreviousYear, contractualNetValueStartYear).doubleValue();
        return calculateGrantAmortizationRemainsEndYearJava(vehicle, grantType, year, grantAmortizationRemainsStartYear, grantAmortization);
    }

    public BigDecimal calculateGrantAmortizationRemainsEndYearDrools(Vehicle vehicle, Vehicle.GrantType grantType, ContractualYear contractualYear, BigDecimal amortizationDuration, BigDecimal grantAmortizationRemainsStartYearPreviousYear, BigDecimal grantAmortizationPreviousYear, BigDecimal contractualNetValueStartYear) {
        KieContainer kieContainer = new DroolsConfigTwo().kieContainer();
        KieSession kieSession = kieContainer.newKieSession("GeodeDroolsCalcSession");

        try {
            kieSession.insert(vehicle);
            kieSession.insert(contractualYear);
            kieSession.fireAllRules();
        } finally {
            kieSession.dispose();
        }
        return vehicle.getGrantAmortizationRemainsEndYear();
    }

    public long calculationTimeOfGrantAmortizationRemainsEndYearDroolsStateful_ImportedObjects(List<Vehicle> vehicles, Vehicle.GrantType grantType, List<ContractualYear> contractualYears, BigDecimal amortizationDuration, BigDecimal grantAmortizationRemainsStartYearPreviousYear, BigDecimal grantAmortizationPreviousYear, BigDecimal contractualNetValueStartYear) {
        KieSession kieSession = kieContainer.newKieSession("GeodeDroolsCalcSession");

        LocalDateTime startDate = LocalDateTime.now();
        try {
            for (ContractualYear contractualYear : contractualYears) {
                kieSession.insert(contractualYear);
            }
            for (Vehicle vehicle : vehicles) {
                kieSession.insert(vehicle);
            }
            kieSession.fireAllRules();
        } finally {
            kieSession.dispose();
        }
        LocalDateTime endDate = LocalDateTime.now();
        return Duration.between(startDate, endDate).toMillis();
    }

    public long calculationTimeOfGrantAmortizationRemainsEndYearDroolsStateless_ImportedObjects(List<Command> cmd, Vehicle.GrantType grantType, BigDecimal amortizationDuration, BigDecimal grantAmortizationRemainsStartYearPreviousYear, BigDecimal grantAmortizationPreviousYear, BigDecimal contractualNetValueStartYear) {
        KieContainer kieContainer = new DroolsConfigTwo().kieContainer();
        KieSession kieSession = kieContainer.newKieSession("GeodeDroolsCalcSession");

        LocalDateTime startDate = LocalDateTime.now();
        kieSession.execute(CommandFactory.newBatchExecution(cmd));
        LocalDateTime endDate = LocalDateTime.now();
        return Duration.between(startDate, endDate).toMillis();
    }

    public BigDecimal statefulScanCalculateGrantAmortizationRemainsStartYearDrools(Vehicle vehicle, Vehicle.GrantType grantType, ContractualYear year, BigDecimal amortizationDuration, BigDecimal grantAmortizationRemainsStartYearPreviousYear, BigDecimal grantAmortizationPreviousYear) {
        KieSession kieSession = kieContainer.newKieSession("GeodeDroolsCalcSession");

        try {
            kieSession.insert(vehicle);
            kieSession.insert(year);
            kieSession.fireAllRules();
        } finally {
            kieSession.dispose();
        }
        return vehicle.getGrantAmortizationRemainsEndYear();
    }
}
