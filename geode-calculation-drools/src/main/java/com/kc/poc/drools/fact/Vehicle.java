package com.kc.poc.drools.fact;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.kc.poc.drools.model.ModelType;
import com.kc.poc.drools.proposition.PropositionElement;
import com.kc.poc.drools.service.ITypeStrategy;
import com.kc.poc.drools.service.VehicleStrategy;
import com.kc.poc.drools.strategies.IVehicleStrategy;
import com.kc.poc.drools.strategies.VehicleStrategy2021;
import jakarta.persistence.*;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;
import org.springframework.beans.BeanUtils;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Collectors;

@Entity
@Table(name = "T_VEHICLE")
public class Vehicle extends PropositionElement implements ITypeStrategy<IVehicleStrategy> {

    public enum GrantType {
        STIF,
        OTHER
    }

    private static final Map<ModelType, IVehicleStrategy> STRATEGIES = new HashMap<>();
    {
        STRATEGIES.put(ModelType.DSP_2021, new VehicleStrategy2021());
        STRATEGIES.put(ModelType.DSP, new VehicleStrategy());
        STRATEGIES.put(ModelType.AD_HOC, new VehicleStrategy());
        STRATEGIES.put(ModelType.MARCHE_PUBLIC, new VehicleStrategy());
    }

    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proposition_id")
    private Proposition proposition;

    @JsonView({ JsonViews.ProvisionalOperatingAccount.class })
    // to keep order
    private Integer number;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "type_id")
    @JsonView({ JsonViews.ProvisionalOperatingAccount.class })
    private VehicleType type;

    @JsonView({ JsonViews.ProvisionalOperatingAccount.class })
    private String registrationNumber;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @JsonDeserialize(using = ISO8601LocalDateDeserializer.class)
    @JsonView({ JsonViews.ProvisionalOperatingAccount.class })
    private LocalDate purchaseDate;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @JsonDeserialize(using = ISO8601LocalDateDeserializer.class)
    @JsonView({ JsonViews.ProvisionalOperatingAccount.class })
    private LocalDate startDate;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @JsonDeserialize(using = ISO8601LocalDateDeserializer.class)
    @JsonView({ JsonViews.ProvisionalOperatingAccount.class })
    private LocalDate endDate;

    @JsonView({ JsonViews.ProvisionalOperatingAccount.class })
    private Boolean accessible;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "euronorm_id")
    @JsonView({ JsonViews.ProvisionalOperatingAccount.class })
    private EuroNorm euroNorm;

    @JsonView({ JsonViews.ProvisionalOperatingAccount.class })
    private Boolean newVehicle;

    @JsonView({ JsonViews.ProvisionalOperatingAccount.class })
    private Integer startDateValue = 0;

    @Column(precision = 12, scale = 2)
    @JsonView({ JsonViews.ProvisionalOperatingAccount.class })
    private BigDecimal stifGrantPercentage = BigDecimal.ZERO;

    @Column(precision = 12, scale = 2)
    @JsonView({ JsonViews.ProvisionalOperatingAccount.class })
    private BigDecimal otherGrantPercentage = BigDecimal.ZERO;

    @OneToMany(mappedBy = "vehicle", fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    @MapKey(name = "year")
    @JsonView({ JsonViews.ProvisionalOperatingAccount.class })
    private Map<Integer, VehicleYearData> yearData = new HashMap<>();

    @OneToOne(mappedBy = "vehicle", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JsonView({ JsonViews.ProvisionalOperatingAccount.class })
    private VehicleSummary summary;

    @JsonView({ JsonViews.ProvisionalOperatingAccount.class })
    private Boolean vehiculeIsIdfm;

    public Vehicle() {
        super();
    }

    public Vehicle(Proposition proposition, VehicleType type, String registrationNumber, LocalDate purchaseDate, LocalDate startDate, LocalDate endDate,
                   VehicleFunction function, Boolean accessible, EuroNorm euroNorm, Boolean newVehicle, Integer startDateValue, BigDecimal stifGrantPercentage,
                   BigDecimal otherGrantPercentage, BigDecimal allocationPercentage, Boolean vehiculeIsIdfm) {
        this();
        this.proposition = proposition;
        this.type = type;
        this.registrationNumber = registrationNumber;
        this.purchaseDate = purchaseDate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.accessible = accessible;
        this.euroNorm = euroNorm;
        this.newVehicle = newVehicle;
        this.startDateValue = startDateValue;
        this.stifGrantPercentage = stifGrantPercentage;
        this.otherGrantPercentage = otherGrantPercentage;
        this.vehiculeIsIdfm = vehiculeIsIdfm;
        for (ContractualYear year : proposition.getYears()) {
            VehicleYearData vehicleYearData = new VehicleYearData();
            vehicleYearData.setAllocationPercentage(allocationPercentage);
            vehicleYearData.setFunction(function);
            vehicleYearData.setYear(year.getNumber());
            yearData.put(year.getNumber(), vehicleYearData);
        }
    }

    public boolean isAccessible() {
        return accessible != null && accessible;
    }

    public boolean isNewVehicle() {
        return newVehicle != null && newVehicle;
    }

    @JsonProperty("grantNetValue")
    @JsonView({ JsonViews.ProvisionalOperatingAccount.class })
    public BigDecimal getGrantNetValue() {
        if (startDateValue != null) {
            return MathUtil.asBigDecimal(startDateValue).multiply(BigDecimal.ONE.subtract(stifGrantPercentage).subtract(otherGrantPercentage));
        } else {
            return BigDecimal.ZERO;
        }

    }

    @JsonProperty("stifGrant")
    @JsonView({ JsonViews.ProvisionalOperatingAccount.class })
    public BigDecimal getStifGrant() {
        return getGrant(GrantType.STIF);
    }

    @JsonProperty("otherGrant")
    @JsonView({ JsonViews.ProvisionalOperatingAccount.class })
    public BigDecimal getOtherGrant() {
        return getGrant(GrantType.OTHER);
    }

    public void setRelationships() {
        if (summary != null) {
            summary.setVehicle(this);
            summary.setRelationships();
        }
        for (Entry<Integer, VehicleYearData> entry : yearData.entrySet()) {
            VehicleYearData vehicleYearData = entry.getValue();
            vehicleYearData.setVehicle(this);
            vehicleYearData.setYear(entry.getKey());
        }
    }

    @JsonIgnore
    public LocalDate getEndDateEndOfMonth() {
        LocalDate endDateEndOfMonth = null;
        if (endDate != null) {
            endDateEndOfMonth = endDate.dayOfMonth().withMaximumValue();
        }
        return endDateEndOfMonth;
    }

    public BigDecimal getGrantPercentage(GrantType grantType) {
        BigDecimal grantPercentage;
        switch (grantType) {
            case OTHER:
                grantPercentage = getOtherGrantPercentage();
                break;
            case STIF:
                grantPercentage = getStifGrantPercentage();
                break;
            default:
                grantPercentage = null;
                break;
        }
        return grantPercentage;
    }

    public Vehicle deepCloneEntity(VehicleType newVehicleType, Proposition target) {
        Vehicle newVehicle = new Vehicle();
        BeanUtils.copyProperties(this, newVehicle, getIgnoredPropertyNames(this));
        // Summary
        if (this.summary != null) {
            VehicleSummary newVehicleSummary = this.summary.deepCloneEntity(newVehicle, target);
            newVehicle.setSummary(newVehicleSummary);
        } else {
            newVehicle.setSummary(null);
        }
        // Years data
        HashMap<Integer, VehicleYearData> newVehicleYearDataSet = new HashMap<Integer, VehicleYearData>();
        for (Entry<Integer, VehicleYearData> yearDataEntry : this.yearData.entrySet()) {
            VehicleYearData newVehicleYearData = yearDataEntry.getValue().deepCloneEntity(newVehicle);
            newVehicleYearDataSet.put(yearDataEntry.getKey(), newVehicleYearData);
        }
        newVehicle.setYearData(newVehicleYearDataSet);
        // Type
        newVehicle.setType(newVehicleType);
        newVehicle.setProposition(target);
        return newVehicle;
    }

    @JsonIgnore
    public BigDecimal getGrant(GrantType grantType) {
        BigDecimal grantPercentage = getGrantPercentage(grantType);
        if (startDateValue != null && grantPercentage != null) {
            return MathUtil.asBigDecimal(startDateValue).multiply(grantPercentage);
        } else {
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal calculateAmortizationDuration() {
        return this.getStrategy(this.getProposition()).calculateAmortizationDuration(this);
    }

    public BigDecimal calculateDurationInContractualYear(ContractualYear year) {
        return this.getStrategy(this.getProposition()).calculateDurationInContractualYear(this, year);
    }

    public BigDecimal calculateAge(ContractualYear year) {
        return this.getStrategy(this.getProposition()).calculateAge(this, year);
    }

    public BigDecimal calculateDurationInContract(ContractualYear year) {
        BigDecimal durationInContract = BigDecimal.ZERO;
        LocalDate startDate = getStartDate();
        // LocalDate contractStartDate = proposition.getStartDate();
        if (startDate != null) {
            BigDecimal durationInContractualYear = calculateDurationInContractualYear(year);
            BigDecimal durationInLastContractualYear = BigDecimal.ZERO;
            if (getSummary() != null && getSummary().getYearData(year.getNumber() - 1) != null) {
                durationInLastContractualYear = getSummary().getYearData(year.getNumber() - 1).getDurationInContract();
            }
            if (startDate.isBefore(year.getStartDate()) && durationInLastContractualYear != null) {
                durationInContract = durationInContractualYear.add(durationInLastContractualYear);
            } else {
                durationInContract = durationInContractualYear;
            }
        }
        return durationInContract;
    }

    public BigDecimal calculateDam(ContractualYear year, BigDecimal amortizationDuration, BigDecimal contractualNetValueStartYear) {
        double dam = 0;
        double amortization = MathUtil.asDoubleValue(calculateAmortization(year, amortizationDuration, contractualNetValueStartYear));
        double allocationPercentage = MathUtil.asDoubleValue(this.getAllocationPercentage(year.getNumber()));
        double stifGrantAmortization = MathUtil.asDoubleValue(calculateStifGrantAmortization(year, amortizationDuration, contractualNetValueStartYear));
        double otherGrantAmortization = MathUtil.asDoubleValue(calculateOtherGrantAmortization(year, amortizationDuration, contractualNetValueStartYear));
        dam = amortization * allocationPercentage - stifGrantAmortization - otherGrantAmortization;
        return MathUtil.asBigDecimal(dam);
    }

    public BigDecimal calculateAmortization(ContractualYear year, BigDecimal amortizationDuration, BigDecimal contractualNetValueStartYear) {
        return this.getStrategy(this.getProposition()).calculateAmortization(this, year, amortizationDuration, contractualNetValueStartYear);
    }

    /**
     * Amortissement / reprise de la subvention avec affectation
     */
    public BigDecimal calculateGrantAmortization(GrantType grantType, ContractualYear year, BigDecimal amortizationDuration, BigDecimal contractualNetValueStartYear) {
        return this.getStrategy(this.getProposition()).calculateGrantAmortization(this, grantType, year, amortizationDuration, contractualNetValueStartYear);
    }

    /**
     * Reste à amortir / à reprendre de la subvention au début de l'année
     */
    public BigDecimal calculateGrantAmortizationRemainsStartYear(GrantType grantType, ContractualYear year,
                                                                 BigDecimal grantAmortizationRemainsStartYearPreviousYear, BigDecimal grantAmortizationPreviousYear, BigDecimal contractualNetValueStartYear) {
        return this.getStrategy(this.getProposition()).calculateGrantAmortizationRemainsStartYear(this, grantType, year, grantAmortizationRemainsStartYearPreviousYear,
                grantAmortizationPreviousYear, contractualNetValueStartYear);
    }

    /**
     * Reste à reprendre de la subvention avec affectation à la fin de l'année
     */
    public BigDecimal calculateGrantAmortizationRemainsEndYear(GrantType grantType, ContractualYear year, BigDecimal amortizationDuration,
                                                               BigDecimal grantAmortizationRemainsStartYearPreviousYear, BigDecimal grantAmortizationPreviousYear, BigDecimal contractualNetValueStartYear) {
        return this.getStrategy(this.getProposition()).calculateGrantAmortizationRemainsEndYear(this, grantType, year, amortizationDuration,
                grantAmortizationRemainsStartYearPreviousYear, grantAmortizationPreviousYear, contractualNetValueStartYear);
    }


    public BigDecimal calculateStifGrantAmortization(ContractualYear year, BigDecimal amortizationDuration, BigDecimal contractualNetValueStartYear) {
        return calculateGrantAmortization(GrantType.STIF, year, amortizationDuration, contractualNetValueStartYear);
    }

    public BigDecimal calculateStifGrantAmortizationRemainsStartYear(ContractualYear year, BigDecimal stifGrantAmortizationRemainsStartYearPreviousYear,
                                                                     BigDecimal stifGrantAmortizationPreviousYear, BigDecimal contractualNetValueStartYear) {
        return calculateGrantAmortizationRemainsStartYear(GrantType.STIF, year, stifGrantAmortizationRemainsStartYearPreviousYear,
                stifGrantAmortizationPreviousYear, contractualNetValueStartYear);
    }

    public BigDecimal calculateStifGrantAmortizationRemainsEndYear(ContractualYear year, BigDecimal amortizationDuration,
                                                                   BigDecimal stifGrantAmortizationRemainsStartYearPreviousYear, BigDecimal stifGrantAmortizationPreviousYear, BigDecimal contractualNetValueStartYear) {
        return calculateGrantAmortizationRemainsEndYear(GrantType.STIF, year, amortizationDuration, stifGrantAmortizationRemainsStartYearPreviousYear,
                stifGrantAmortizationPreviousYear, contractualNetValueStartYear);
    }

    public BigDecimal calculateOtherGrantAmortization(ContractualYear year, BigDecimal amortizationDuration, BigDecimal contractualNetValueStartYear) {
        return calculateGrantAmortization(GrantType.OTHER, year, amortizationDuration, contractualNetValueStartYear);
    }

    public BigDecimal calculateOtherGrantAmortizationRemainsStartYear(ContractualYear year, BigDecimal otherGrantAmortizationRemainsStartYearPreviousYear,
                                                                      BigDecimal otherGrantAmortizationPreviousYear, BigDecimal contractualNetValueStartYear) {
        return calculateGrantAmortizationRemainsStartYear(GrantType.OTHER, year, otherGrantAmortizationRemainsStartYearPreviousYear,
                otherGrantAmortizationPreviousYear, contractualNetValueStartYear);
    }

    public BigDecimal calculateOtherGrantAmortizationRemainsEndYear(ContractualYear year, BigDecimal amortizationDuration,
                                                                    BigDecimal otherGrantAmortizationRemainsStartYearPreviousYear, BigDecimal otherGrantAmortizationPreviousYear, BigDecimal contractualNetValueStartYear) {
        return calculateGrantAmortizationRemainsEndYear(GrantType.OTHER, year, amortizationDuration, otherGrantAmortizationRemainsStartYearPreviousYear,
                otherGrantAmortizationPreviousYear, contractualNetValueStartYear);
    }

    public BigDecimal calculateContractualNetValueStartYear(ContractualYear year, BigDecimal contractualNetValueStartYearPreviousYear,
                                                            BigDecimal damPreviousYear, BigDecimal stifGrantAmortizationPreviousYear, BigDecimal otherGrantAmortizationPreviousYear) {
        return this.getStrategy(this.getProposition()).calculateContractualNetValueStartYear(this, year, contractualNetValueStartYearPreviousYear, damPreviousYear, stifGrantAmortizationPreviousYear, otherGrantAmortizationPreviousYear);
    }

    public boolean shouldUseEndYearAmortizationInsteadOfAmortizationIfLower() {
        return this.getStrategy(this.getProposition()).shouldUseEndYearAmortizationInsteadOfAmortizationIfLower();
    }

    public BigDecimal calculateContractualNetValueWithAllocationStartYear(ContractualYear year, BigDecimal contractualNetValueStartYear) {
        double allocationPercentage = MathUtil.asDoubleValue(getAllocationPercentage(year.getNumber()));
        BigDecimal contractualNetValueStartYearWithAllocation = MathUtil
                .asBigDecimal(MathUtil.asDoubleValue(contractualNetValueStartYear) * allocationPercentage);
        return contractualNetValueStartYearWithAllocation;
    }

    public BigDecimal calculateContractualNetValueWithAllocationEndYear(ContractualYear year, BigDecimal amortizationDuration,
                                                                        BigDecimal contractualNetValueStartYear) {
        double contractualNetValueEndYearWithAllocation = 0;
        double amortization = MathUtil.asDoubleValue(calculateAmortization(year, amortizationDuration, contractualNetValueStartYear));
        double allocationPercentage = MathUtil.asDoubleValue(getAllocationPercentage(year.getNumber()));
        contractualNetValueEndYearWithAllocation = (MathUtil.asDoubleValue(contractualNetValueStartYear) - amortization) * allocationPercentage;
        return MathUtil.asBigDecimal(contractualNetValueEndYearWithAllocation);
    }

    @Deprecated
    public BigDecimal calculateFinancialFees(ContractualYear year, BigDecimal amortizationDuration, BigDecimal debtFinancingAverageRate,
                                             BigDecimal contractualNetValueStartYearPreviousYear, BigDecimal damPreviousYear, //
                                             BigDecimal stifGrantAmortizationRemainsStartYearPreviousYear, BigDecimal stifGrantAmortizationPreviousYear, //
                                             BigDecimal otherGrantAmortizationRemainsStartYearPreviousYear, BigDecimal otherGrantAmortizationPreviousYear) {
        return this.getStrategy(this.getProposition()).calculateFinancialFees(this, year, amortizationDuration, debtFinancingAverageRate, contractualNetValueStartYearPreviousYear,
                damPreviousYear, stifGrantAmortizationRemainsStartYearPreviousYear, stifGrantAmortizationPreviousYear, otherGrantAmortizationRemainsStartYearPreviousYear, otherGrantAmortizationPreviousYear);
    }

    public BigDecimal calculateFinancialFees(ContractualYear year, BigDecimal amortizationDuration, BigDecimal debtFinancingAverageRate,
                                             BigDecimal contractualNetValueStartYear, BigDecimal dam, BigDecimal stifGrantAmortizationRemainsStartYear,
                                             BigDecimal otherGrantAmortizationRemainsStartYear) {
        return this.getStrategy(this.getProposition()).calculateFinancialFees(this, year, amortizationDuration, debtFinancingAverageRate, contractualNetValueStartYear,
                dam, stifGrantAmortizationRemainsStartYear, otherGrantAmortizationRemainsStartYear);
    }

    public boolean isStartDateInYear(ContractualYear year) {
        LocalDate vehicleStartDate = getStartDate();
        boolean startDateInYear = !vehicleStartDate.isBefore(year.getStartDate()) && !vehicleStartDate.isAfter(year.getEndDate());
        return startDateInYear;
    }

    /** get the usage ratio of the equipment for the specified year. */
    public BigDecimal calculateUsageRatio(ContractualYear year) {
        return this.getStrategy(this.getProposition()).calculateUsageRatio(this, year);
    }

    @JsonIgnore
    public BigDecimal getAllocationPercentage(int yearNumber) {
        BigDecimal allocationPercentage = null;
        if (yearData.get(yearNumber) != null) {
            allocationPercentage = yearData.get(yearNumber).getAllocationPercentage();
        }
        return allocationPercentage;
    }

    @JsonIgnore
    public void setAllocationPercentage(int yearNumber, BigDecimal allocationPercentage) {
        VehicleYearData yearDataEntry = yearData.get(yearNumber);
        if (yearDataEntry == null) {
            yearDataEntry = new VehicleYearData(yearNumber);
            yearData.put(yearNumber, yearDataEntry);
        }
        yearDataEntry.setAllocationPercentage(allocationPercentage);
    }

    /**
     * [Used quantity](UO) = [usage ratio] * [allocation percentage]
     *
     * @param yearNumber
     * @return
     */
    public BigDecimal getUsedQuantity(int yearNumber) {
        return MathUtil.denullify(getUsageRatio(yearNumber)).multiply(MathUtil.denullify(getAllocationPercentage(yearNumber)));
    }

    public Map<Integer, BigDecimal> getUsedQuantityPerYear(BiPredicate<Vehicle, Integer> condition) {
        return yearData.keySet().stream().filter(y -> condition.test(this, y)).collect(Collectors.toMap(y -> y, this::getUsedQuantity));
    }

    public Optional<BigDecimal> getAllocationPercentageOptional(int yearNumber) {
        return getYearDataValue(yearNumber, VehicleYearData::getAllocationPercentage);
    }

    /** La fonction de vehicule dans l'annee donnee est bien <code>function</code> ? */
    public boolean isFunction(VehicleFunction function, int yearNumber) {
        return getYearDataValue(yearNumber, yd -> yd.isFunction(function)).orElse(Boolean.FALSE);
    }

    private <T> Optional<T> getYearDataValue(Integer yearNumber, Function<VehicleYearData, T> getter) {
        VehicleYearData yearDataEntry = yearData.get(yearNumber);
        if (yearData != null) {
            return Optional.ofNullable(getter.apply(yearDataEntry));
        }
        return Optional.empty();
    }

    @JsonIgnore
    public VehicleFunction getFunction(int yearNumber) {
        VehicleFunction function = null;
        if (yearData.get(yearNumber) != null) {
            function = yearData.get(yearNumber).getFunction();
        }
        return function;
    }

    public String getFunctionName(int yearNumber) {
        VehicleFunction function = getFunction(yearNumber);
        if (function != null) {
            return function.name();
        }
        return null;
    }

    /**
     * Valeur Nette Contractuelle du véhicule au début de l'année pour l'annee donnee
     */
    public Optional<BigDecimal> getContractualNetValueStartYear(Integer yearNumber) {
        return getSummaryValue(yearNumber, VehicleSummary::getContractualNetValueStartYear);
    }

    public Optional<BigDecimal> getContractualNetValueStartYearWithAllocation(Integer yearNumber) {
        return getSummaryValue(yearNumber, VehicleSummary::getContractualNetValueStartYearWithAllocation);
    }

    /**
     * Valeur Nette Contractuelle du véhicule avec affectation à la fin de l'année pour l'annee donnee
     */
    public Optional<BigDecimal> getContractualNetValueEndYearWithAllocation(Integer yearNumber) {
        return getSummaryValue(yearNumber, VehicleSummary::getContractualNetValueEndYearWithAllocation);
    }

    /**
     * DAM annuelle nettes de subvention avec affectation pour l'annee donnee
     */
    public Optional<BigDecimal> getDam(Integer yearNumber) {
        return getSummaryValue(yearNumber, VehicleSummary::getDam);
    }

    /**
     * Frais financiers avec affectation pour l'annee donnee
     */
    public Optional<BigDecimal> getFinancialFees(Integer yearNumber) {
        return getSummaryValue(yearNumber, VehicleSummary::getFinancialFees);
    }

    /**
     * Age pour l'annee donnee
     */
    public Optional<BigDecimal> getAge(Integer yearNumber) {
        return getSummaryValue(yearNumber, VehicleSummary::getAge);
    }

    /**
     * Amortissement / reprise de la subvention avec affectation pour l'annee donnee
     */
    public Optional<BigDecimal> getStifGrantAmortization(Integer yearNumber) {
        return getSummaryValue(yearNumber, VehicleSummary::getStifGrantAmortization);
    }

    /**
     * Reste à reprendre de la subvention avec affectation à la fin de l'année pour l'annee donnee
     */
    public Optional<BigDecimal> getStifGrantAmortizationRemainsEndYear(Integer yearNumber) {
        return getSummaryValue(yearNumber, VehicleSummary::getStifGrantAmortizationRemainsEndYear);
    }

    /**
     * Autres subventions
     * Amortissement / reprise de la subvention avec affectation pour l'annee donnee
     */
    public Optional<BigDecimal> getOtherGrantAmortization(Integer yearNumber) {
        return getSummaryValue(yearNumber, VehicleSummary::getOtherGrantAmortization);
    }

    /**
     *  Autres subventions
     *  Reste à reprendre de la subvention avec affectation à la fin de l'année
     */
    public Optional<BigDecimal> getOtherGrantAmortizationRemainsEndYear(Integer yearNumber) {
        return getSummaryValue(yearNumber, VehicleSummary::getOtherGrantAmortizationRemainsEndYear);
    }

    public Optional<BigDecimal> getUsageRatio(Integer yearNumber) {
        return getSummaryValue(yearNumber, VehicleSummary::getUsageRatio);
    }

    private <T> Optional<T> getSummaryValue(Integer yearNumber, BiFunction<VehicleSummary, Integer, Optional<T>> getter) {
        if (summary != null) {
            return getter.apply(summary, yearNumber);
        }
        return Optional.empty();
    }

    /** retourne vrai si la date de sortie du véhicule dépasse la date actuelle **/
    @JsonProperty("isVehicleOutOfParc")
    @JsonView({ JsonViews.ProvisionalOperatingAccount.class })
    public boolean isVehicleOutOfParc() {
        // Mantis 25284, reopen 2019-11-15: la date n'a pas de jour précis, la précision est mois.
        // Donc si ajd est nov. 2019, et la date de sortie est nov. 2019, le véhicule n'est pas encore sorti.
        LocalDate firstDayOfCurrentMonth = LocalDate.now().withDayOfMonth(1);
        return endDate.isBefore(firstDayOfCurrentMonth);
    }

    @Override
    public Map<ModelType, IVehicleStrategy> strategies() {
        return STRATEGIES;
    }

    // Getters et Setters triviaux
    public Proposition getProposition() {
        return proposition;
    }

    public void setProposition(Proposition proposition) {
        this.proposition = proposition;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public VehicleType getType() {
        return type;
    }

    public void setType(VehicleType type) {
        this.type = type;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Boolean getAccessible() {
        return accessible;
    }

    public void setAccessible(Boolean accessible) {
        this.accessible = accessible;
    }

    public EuroNorm getEuroNorm() {
        return euroNorm;
    }

    public void setEuroNorm(EuroNorm euroNorm) {
        this.euroNorm = euroNorm;
    }

    public Boolean getNewVehicle() {
        return newVehicle;
    }

    public void setNewVehicle(Boolean newVehicle) {
        this.newVehicle = newVehicle;
    }

    public Integer getStartDateValue() {
        return startDateValue;
    }

    public void setStartDateValue(Integer startDateValue) {
        this.startDateValue = startDateValue;
    }

    public BigDecimal getStifGrantPercentage() {
        return stifGrantPercentage;
    }

    public void setStifGrantPercentage(BigDecimal stifGrantPercentage) {
        this.stifGrantPercentage = stifGrantPercentage;
    }

    public BigDecimal getOtherGrantPercentage() {
        return otherGrantPercentage;
    }

    public void setOtherGrantPercentage(BigDecimal otherGrantPercentage) {
        this.otherGrantPercentage = otherGrantPercentage;
    }

    public Boolean getVehiculeIsIdfm() {
        if(vehiculeIsIdfm == null)
            return false;
        return vehiculeIsIdfm;
    }

    public void setVehiculeIsIdfm(Boolean vehiculeIsIdfm) {
        this.vehiculeIsIdfm = vehiculeIsIdfm;
    }

    public VehicleSummary getSummary() {
        return summary;
    }

    public void setSummary(VehicleSummary summary) {
        this.summary = summary;
    }

    public Map<Integer, VehicleYearData> getYearData() {
        return yearData;
    }

    public void setYearData(Map<Integer, VehicleYearData> yearData) {
        this.yearData = yearData;
    }
}
