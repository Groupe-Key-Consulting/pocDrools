package drools;

import com.kc.poc.drools.fact.Vehicle;
import com.kc.poc.drools.strategies.VehicleStrategy2021Two;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculateAmortizationDurationTestTwo {

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
        BigDecimal result = new VehicleStrategy2021Two().calculateAmortizationDurationDrools(vehicle);

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
        BigDecimal result = new VehicleStrategy2021Two().calculateAmortizationDurationDrools(vehicle);

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
        BigDecimal result = new VehicleStrategy2021Two().calculateAmortizationDurationDrools(vehicle).setScale(4, RoundingMode.HALF_UP);
        System.out.println(result);
        // Then
        // 1.0833 = 2 - (13/12)
      assertEquals(BigDecimal.valueOf(1.0833), result);
    }
}
