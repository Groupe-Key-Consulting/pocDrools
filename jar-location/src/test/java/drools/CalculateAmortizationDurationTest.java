package drools;

import com.kc.poc.drools.fact.Vehicle;
import com.kc.poc.drools.service.VehicleStrategy2021Service;
import org.junit.jupiter.api.Test;
import org.kie.api.command.Command;
import org.kie.internal.command.CommandFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculateAmortizationDurationTest {

    @Test
    public void should_calculate_amortization_duration_when_new_vehicle_is_true_and_amortization_period_is_greater_than_zero() {
        // Given
        LocalDate date = LocalDate.now();
        Vehicle vehicle = new Vehicle();
        vehicle.setNewVehicle(true);
        vehicle.setPurchaseDate(date.minusYears(2));
        vehicle.setStartDate(date.minusYears(1));
        vehicle.setNewVehiclesAmortizationPeriod(5);
        vehicle.setOldVehiclesAmortizationPeriod(0);

        // When
        BigDecimal result = new VehicleStrategy2021Service().calculateAmortizationDurationDrools(vehicle);

        // Then
        assertEquals(BigDecimal.valueOf(5.0), result);
    }

    @Test
    public void should_calculate_amortization_duration_when_new_vehicle_is_true_and_amortization_period_is_zero() {
        // Given
        LocalDate date = LocalDate.now();
        Vehicle vehicle = new Vehicle();
        vehicle.setNewVehicle(true);
        vehicle.setPurchaseDate(date.minusYears(2));
        vehicle.setStartDate(date.minusYears(1));
        vehicle.setNewVehiclesAmortizationPeriod(0);
        vehicle.setOldVehiclesAmortizationPeriod(0);

        // When
        BigDecimal result = new VehicleStrategy2021Service().calculateAmortizationDurationDrools(vehicle);

        // Then
        assertEquals(BigDecimal.valueOf(0.0), result);
    }

    @Test
    public void should_calculate_amortization_duration_when_new_vehicle_is_false_and_amortization_period_is_greater_than_zero() {
        // Given
        // drl file --> calculateAmortizationDuration.drl
        LocalDate now = LocalDate.now();
        Vehicle vehicle = new Vehicle();
        vehicle.setNewVehicle(false);
        vehicle.setPurchaseDate(now.minusYears(2));
        vehicle.setStartDate(now.minusYears(1).minusMonths(1));
        vehicle.setNewVehiclesAmortizationPeriod(0);
        vehicle.setOldVehiclesAmortizationPeriod(2);

        // When
        BigDecimal result = new VehicleStrategy2021Service().calculateAmortizationDurationDrools(vehicle).setScale(4, RoundingMode.HALF_UP);
        System.out.println(result);
        // Then
        // 1.0833 = 2 - (13/12)
        assertEquals(BigDecimal.valueOf(1.0833), result);
    }

    /*-------------------------------------------------------------------------------------------------*/
    /*-------------------------------------------------------------------------------------------------*/
    /*------------------------------       CALCULATION TIME TESTS        ------------------------------*/
    /*-------------------------------------------------------------------------------------------------*/
    /*-------------------------------------------------------------------------------------------------*/
    @Test
    public void should_get_calculation_time_of_amortization_duration_by_batch_of_million_vehicle_in_stateless_session() {
        // Given
        List<Command> cmd = new ArrayList<>();
        LocalDate now = LocalDate.now();

        for (int i = 0; i < 1000000; i++) {
            boolean oldVehicle = new Random().nextBoolean();
            cmd.add(CommandFactory.newInsert(new Vehicle(
                    now.minusYears(2).minusMonths(new Random().nextInt(12)),
                    oldVehicle,
                    now.minusYears(1).minusMonths(new Random().nextInt(12)),
                    oldVehicle ? 0 : new Random().nextInt(5),
                    oldVehicle ? new Random().nextInt(5) : 0,
                    0,
                    null
            )));
        }

        // When
        long duration = new VehicleStrategy2021Service().calculationTimeOfAmortizationDurationStateless(cmd);

        // Then
        System.out.println("Calculation time of amortization duration for 1.000.000 Vehicle in Stateless Session: " + duration);
    }

    @Test
    public void should_get_calculation_time_of_amortization_duration_by_batch_of_million_vehicle_in_multiple_stateless_session() {
        // Given
        List<Command> cmd = new ArrayList<>();
        LocalDate now = LocalDate.now();

        for (int i = 0; i < 1000000; i++) {
            boolean oldVehicle = new Random().nextBoolean();
            cmd.add(CommandFactory.newInsert(new Vehicle(
                    now.minusYears(2).minusMonths(new Random().nextInt(12)),
                    oldVehicle,
                    now.minusYears(1).minusMonths(new Random().nextInt(12)),
                    oldVehicle ? 0 : new Random().nextInt(5),
                    oldVehicle ? new Random().nextInt(5) : 0,
                    0,
                    null
            )));
        }

        // When
        long duration = new VehicleStrategy2021Service().calculationTimeOfAmortizationDurationMultipleStateless(cmd);

        // Then
        System.out.println("Calculation time of amortization duration for 1.000.000 Vehicle in Stateless Session: " + duration);
    }

    @Test
    public void should_get_calculation_time_of_amortization_duration_by_batch_of_million_vehicle_in_stateful_session_with_given() {
        // Given
        List<Command> cmd = new ArrayList<>();
        LocalDate now = LocalDate.now();

        for (int i = 0; i < 1000000; i++) {
            boolean oldVehicle = new Random().nextBoolean();
            cmd.add(CommandFactory.newInsert(new Vehicle(
                    now.minusYears(2).minusMonths(new Random().nextInt(12)),
                    oldVehicle,
                    now.minusYears(1).minusMonths(new Random().nextInt(12)),
                    oldVehicle ? 0 : new Random().nextInt(5),
                    oldVehicle ? new Random().nextInt(5) : 0,
                    0,
                    null
            )));
        }

        // When
        long duration = new VehicleStrategy2021Service().calculationTimeOfAmortizationDurationStateful_ImportedObjects(cmd);

        // Then
        System.out.println("Calculation time of amortization duration for 1.000.000 Vehicle in Stateful Session with given object: " + duration);
    }

    @Test
    public void should_get_calculation_time_of_amortization_duration_by_batch_of_million_vehicle_in_stateful_session_no_given() {
        // Given

        // When
        long duration = new VehicleStrategy2021Service().calculationTimeOfAmortizationDurationStateful_CreateObjectsHere();

        // Then
        System.out.println("Calculation time of amortization duration for 1.000.000 Vehicle in Stateful Session without given object: " + duration);
    }

    @Test
    public void should_get_calculation_time_of_amortization_duration_by_batch_of_million_vehicle_in_java_with_given() {
        // Given
        LocalDate now = LocalDate.now();
        List<Vehicle> vehicles = new ArrayList<>();

        for (int i = 0; i < 1000000; i++) {
            boolean oldVehicle = new Random().nextBoolean();
            vehicles.add(new Vehicle(
                    now.minusYears(2).minusMonths(new Random().nextInt(12)),
                    oldVehicle,
                    now.minusYears(1).minusMonths(new Random().nextInt(12)),
                    oldVehicle ? 0 : new Random().nextInt(5),
                    oldVehicle ? new Random().nextInt(5) : 0,
                    0,
                    null
            ));
        }

        // When
        long duration = new VehicleStrategy2021Service().calculationTimeOfAmortizationDurationJava_ImportedObjects(vehicles);

        // Then
        System.out.println("Calculation time of amortization duration for 1.000.000 Vehicle in Java with given object: " + duration);
    }

    @Test
    public void should_get_calculation_time_of_amortization_duration_by_batch_of_million_vehicle_in_java_no_given() {
        // Given

        // When
        long duration = new VehicleStrategy2021Service().calculationTimeOfAmortizationDurationJava_CreateObjectsHere();

        // Then
        System.out.println("Calculation time of amortization duration for 1.000.000 Vehicle in Java without given object: " + duration);
    }
}