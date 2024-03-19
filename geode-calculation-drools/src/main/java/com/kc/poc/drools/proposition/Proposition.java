package com.kc.poc.drools.proposition;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import info.stif.requeteweb.domain.BaseEntity;
import info.stif.requeteweb.domain.contract.OperatingAccount;
import info.stif.requeteweb.domain.contract.OperatingAccountType;
import info.stif.requeteweb.domain.contract.model.Model;
import info.stif.requeteweb.domain.line.Line;
import info.stif.requeteweb.domain.line.LineData;
import info.stif.requeteweb.domain.line.LineType;
import info.stif.requeteweb.domain.line.SubLine;
import info.stif.requeteweb.domain.proposition.equipments.SAEIVEquipment;
import info.stif.requeteweb.domain.proposition.equipments.SAEIVEquipmentSummary;
import info.stif.requeteweb.domain.proposition.equipments.SAEIVEquipmentType;
import info.stif.requeteweb.domain.proposition.equipments.security.SecurityEquipment;
import info.stif.requeteweb.domain.proposition.equipments.security.SecurityEquipmentSummary;
import info.stif.requeteweb.domain.proposition.equipments.security.SecurityEquipmentType;
import info.stif.requeteweb.domain.proposition.equipments.tickets.TicketsEquipment;
import info.stif.requeteweb.domain.proposition.equipments.tickets.TicketsEquipmentSummary;
import info.stif.requeteweb.domain.proposition.equipments.tickets.TicketsEquipmentType;
import info.stif.requeteweb.domain.proposition.infrastructure.Infrastructure;
import info.stif.requeteweb.domain.proposition.infrastructure.InfrastructureSummary;
import info.stif.requeteweb.domain.proposition.linetrafficrevenue.LineTrafficRevenue;
import info.stif.requeteweb.domain.proposition.marginalcost.MarginalCost;
import info.stif.requeteweb.domain.proposition.revenue.ComplementaryRevenue;
import info.stif.requeteweb.domain.proposition.sitemaintenance.SiteMaintenance;
import info.stif.requeteweb.domain.uoline.LineUo;
import info.stif.requeteweb.domain.util.CustomLocalDateSerializer;
import info.stif.requeteweb.domain.util.ISO8601LocalDateDeserializer;
import info.stif.requeteweb.domain.vehicle.Vehicle;
import info.stif.requeteweb.domain.vehicle.VehicleSummary;
import info.stif.requeteweb.domain.vehicle.VehicleType;
import info.stif.requeteweb.service.contract.ContractualYear;
import info.stif.requeteweb.service.contract.PropositionYear;
import info.stif.requeteweb.web.rest.JsonViews;
import org.hibernate.annotations.DiscriminatorOptions;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Type;
import com.kc.poc.drools.util.DateUtil;
import org.joda.time.LocalDate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "PROPOSITION_TYPE")
@DiscriminatorOptions(force = true)
@Table(name = "T_PROPOSITION")
public abstract class Proposition extends BaseEntity {

	private static final long serialVersionUID = -8290623797168568717L;

	public enum PropositionStatus {
		INITIALIZED,
		COMPLETED,
		SUBMITTED,
		OPENED,
		VALIDATED,
		INVALIDATED,
		CONTRACTUALIZED,
		DELETED
	}

	@JsonView({ JsonViews.class })
	@Enumerated(EnumType.STRING)
	private PropositionStatus status;

	@JsonView({ JsonViews.class })
	private boolean complete = false;

	@JsonView({ JsonViews.class })
	private String name;

	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
	@JsonSerialize(using = CustomLocalDateSerializer.class)
	@JsonDeserialize(using = ISO8601LocalDateDeserializer.class)
	@JsonView({ JsonViews.class })
	protected LocalDate startDate;

	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
	@JsonSerialize(using = CustomLocalDateSerializer.class)
	@JsonDeserialize(using = ISO8601LocalDateDeserializer.class)
	@JsonView({ JsonViews.class })
	protected LocalDate endDate;

	@Column
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	// Not used in front end for now
	@JsonIgnore
	private DateTime contractualDate;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "T_PROPOSITION_LINES", joinColumns = { @JoinColumn(name = "proposition_id") }, inverseJoinColumns = { @JoinColumn(name = "line_id") })
	@JsonView({ JsonViews.ProvisionalOperatingAccount.class, JsonViews.Proposition.class })
	private Set<Line> lines = new HashSet<Line>();

	@OneToMany(mappedBy = "proposition", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JsonView({ JsonViews.ProvisionalOperatingAccount.class, JsonViews.Proposition.class })
	private Set<LineData> linesData = new HashSet<LineData>();

	@OneToMany(mappedBy = "proposition", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JsonView({ JsonViews.ProvisionalOperatingAccount.class, JsonViews.Proposition.class })
	private Set<LineUo> linesUo = new HashSet<LineUo>();

	@OneToMany(mappedBy = "proposition", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JsonView({ JsonViews.ProvisionalOperatingAccount.class, JsonViews.Proposition.class })
	private Set<LineTrafficRevenue> lineTrafficRevenues = new HashSet<LineTrafficRevenue>();

	// If we add this, the relationship is always automatically loaded:
	// http://stackoverflow.com/questions/1444227/making-a-onetoone-relation-lazy
	// @OneToOne(mappedBy = "proposition", fetch = FetchType.LAZY, cascade =
	// CascadeType.REMOVE)
	// @JsonView({ JsonViews.ProvisionalOperatingAccount.class,
	// JsonViews.Proposition.class })
	// private GeneralExpense generalExpense;

	@OneToMany(mappedBy = "proposition", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JsonView({ JsonViews.ProvisionalOperatingAccount.class, JsonViews.Proposition.class })
	@Fetch(FetchMode.SELECT)
	private Set<VehicleType> vehicleTypes = new HashSet<VehicleType>();

	@OneToMany(mappedBy = "proposition", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JsonView({ JsonViews.ProvisionalOperatingAccount.class, JsonViews.Proposition.class })
	private Set<Vehicle> vehicles = new HashSet<Vehicle>();

	@OneToMany(mappedBy = "proposition", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JsonView({ JsonViews.ProvisionalOperatingAccount.class, JsonViews.Proposition.class })
	private Set<VehicleSummary> vehicleSummaries = new HashSet<VehicleSummary>();

	// -- Security Equipments:
	@OneToMany(mappedBy = "proposition", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JsonView({ JsonViews.ProvisionalOperatingAccount.class, JsonViews.Proposition.class })
	private Set<SecurityEquipmentType> securityEquipmentTypes = new HashSet<SecurityEquipmentType>();

	@OneToMany(mappedBy = "proposition", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JsonView({ JsonViews.ProvisionalOperatingAccount.class, JsonViews.Proposition.class })
	private Set<SecurityEquipment> securityEquipments = new HashSet<SecurityEquipment>();

	@OneToMany(mappedBy = "proposition", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JsonView({ JsonViews.ProvisionalOperatingAccount.class, JsonViews.Proposition.class })
	private Set<SecurityEquipmentSummary> securityEquipmentSummaries = new HashSet<SecurityEquipmentSummary>();

	// -- Tickets Equipments:
	@OneToMany(mappedBy = "proposition", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JsonView({ JsonViews.ProvisionalOperatingAccount.class, JsonViews.Proposition.class })
	private Set<TicketsEquipmentType> ticketsEquipmentTypes = new HashSet<TicketsEquipmentType>();

	@OneToMany(mappedBy = "proposition", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JsonView({ JsonViews.ProvisionalOperatingAccount.class, JsonViews.Proposition.class })
	private Set<TicketsEquipment> ticketsEquipments = new HashSet<TicketsEquipment>();

	@OneToMany(mappedBy = "proposition", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JsonView({ JsonViews.ProvisionalOperatingAccount.class, JsonViews.Proposition.class })
	private Set<TicketsEquipmentSummary> ticketsEquipmentSummaries = new HashSet<TicketsEquipmentSummary>();

	// -- SAEIV Equipments:
	@OneToMany(mappedBy = "proposition", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JsonView({ JsonViews.ProvisionalOperatingAccount.class, JsonViews.Proposition.class })
	private Set<SAEIVEquipmentType> saeivEquipmentTypes = new HashSet<SAEIVEquipmentType>();

	@OneToMany(mappedBy = "proposition", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JsonView({ JsonViews.ProvisionalOperatingAccount.class, JsonViews.Proposition.class })
	private Set<SAEIVEquipment> saeivEquipments = new HashSet<SAEIVEquipment>();

	@OneToMany(mappedBy = "proposition", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JsonView({ JsonViews.ProvisionalOperatingAccount.class, JsonViews.Proposition.class })
	private Set<SAEIVEquipmentSummary> saeivEquipmentSummaries = new HashSet<SAEIVEquipmentSummary>();

	@OneToMany(mappedBy = "proposition", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JsonView({ JsonViews.ProvisionalOperatingAccount.class, JsonViews.Proposition.class })
	private Set<ComplementaryRevenue> complementaryRevenues = new HashSet<ComplementaryRevenue>();

	@OneToMany(mappedBy = "proposition", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JsonView({ JsonViews.ProvisionalOperatingAccount.class, JsonViews.Proposition.class })
	private Set<Infrastructure> infrastructures = new HashSet<Infrastructure>();

	@OneToMany(mappedBy = "proposition", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JsonView({ JsonViews.ProvisionalOperatingAccount.class, JsonViews.Proposition.class })
	private Set<InfrastructureSummary> infrastructureSummaries = new HashSet<InfrastructureSummary>();

	@OneToMany(mappedBy = "proposition", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JsonView({ JsonViews.ProvisionalOperatingAccount.class, JsonViews.Proposition.class })
	private Set<SiteMaintenance> siteMaintenances = new HashSet<SiteMaintenance>();

	@JsonView({ JsonViews.ProvisionalOperatingAccount.class, JsonViews.Proposition.class })
	@Column(length = 4096)
	private String comment;

    @JsonView({ JsonViews.ProvisionalOperatingAccount.class, JsonViews.Proposition.class })
	protected abstract OperatingAccountType getOperatinAccountType();

    // FIXME ça devrait être une relation de OneToOne
    // Voir MarginalCostRepository.findByPropositionId(Long) => Une proposition a 1 coût marginal
    @OneToMany(mappedBy = "proposition", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JsonView({ JsonViews.ProvisionalOperatingAccount.class, JsonViews.Proposition.class })
    private Set<MarginalCost> marginalCosts = new HashSet<MarginalCost>();

    @JsonView({ JsonViews.class })
	private boolean anonymized = false;

	// Mantis 25523
	/**
	 * Le but est de distinguer les avenants déjà contractualisés (donc figés) et les nouveaux avenants futurs (contractualisés en 2020 ou après).
	 *
	 * Il s'agit que des anciens modèles.
	 *
	 * null ET ancien modèle ET status == CONTRACTUALIZED  => anciens avenants
	 *
	 * @since 01/2020
	 */
	@JsonIgnore
	private Boolean newContractualized;

	protected Proposition() {
	}

	public Proposition(LocalDate startDate, LocalDate endDate) {
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public abstract OperatingAccount getOperatingAccount();

	@JsonIgnore
	public abstract List<PropositionYear> getPropositionYears();

	@JsonIgnore
	public abstract boolean isNewAmendment();

	@JsonIgnore
	public abstract Model getModel();

	/**
	 * Get PropositionYear de yearNumber en Optional
	 */
	public Optional<PropositionYear> getYearOptional(int yearNumber) {
		List<PropositionYear> years = getPropositionYears();
		return years.stream().filter(y -> y.getNumber() == yearNumber).findFirst();
	}

	/**
	 * get list of relative years Example: for start date = 01/06/2015 and end date = 01/06/2018, you get [0, 1, 2]
	 *
	 * With year 0 = 01/06/2015 to 01/06/2016, year 1 = 01/06/2016 to 01/06/2017, year 2 = 01/06/2017 to 01/06/2018
	 *
	 * @param provisionalOperatingAccount
	 * @return list of relative years
	 */
	@JsonIgnore
	public List<ContractualYear> getYears() {
		LocalDate startDate = getStartDate();
		LocalDate endDate = getEndDate();
		return ContractualYear.getYears(startDate, endDate);
	}

	/**
	 * Year number de cette proposition, commence par 0
	 *
	 * @return
	 */
	@JsonIgnore
	public List<Integer> getYearNumbers() {
		return getYears().stream()
				.map(ContractualYear::getNumber)
				.collect(Collectors.toList());
	}

	/**
	 * Retourner une liste de l'index de l'année saisissable, l'info de saisissable est selon getPropositionYears()
	 *
	 * @see #getPropositionYears()
	 */
	@JsonIgnore
	public List<Integer> getUpdatableYears() {
		return getPropositionYears().stream()
				.filter(PropositionYear::isUpdatable)
				.map(PropositionYear::getNumber)
				.collect(Collectors.toList());
	}

	@JsonIgnore
	public boolean isAdHoc() {
		return getModel().isAdHoc();
	}

	@JsonIgnore
	public boolean isDsp2021() {
		return getModel().isDsp2021();
	}

	@JsonIgnore
	public boolean isOldModel() {
		return getModel().isOldModel();
	}

	/**
	 * Tester si la proposition est à un ancien avenant contractualisé (celui qui est contractualisé donc figé avant 2020)
	 *
	 * La condition :
	 *     L'ancien modèle ET status == CONTRACTUALIZED ET le flag newContractualized == null ou false
	 *
	 * @see #newContractualized
	 *
	 * @return
	 */
	@JsonIgnore
	public boolean isOldContractualized() {
		return getModel().isOldModel() && status == PropositionStatus.CONTRACTUALIZED
				&& (newContractualized == null || !newContractualized);
	}

	public void setRelationships() {
		for (LineData lineData : linesData) {
			lineData.setProposition(this);
			lineData.setRelationships();
		}
	}

	@JsonIgnore
	public List<Line> getLinesSortedByCodification() {
		if (lines != null) {
			return lines.stream().sorted(Comparator.comparing(Line::getCodification, Comparator.nullsLast(Comparator.naturalOrder())))
					.collect(Collectors.toList());
		} else {
			return new ArrayList<Line>();
		}
	}

	@JsonIgnore
	public boolean hasLigneRetiree() {
		for (Line line : lines) {
			if (line.isRetiree()) {
				return true;
			}
		}
		return false;
	}

	public void removeLines() {
		this.lines.clear();
	}

	public void clearLines() {
		this.lines.clear();
	}

	public void addLines(Set<Line> lines) {
		this.lines.addAll(lines);
	}

	public void addLineUo(LineUo lineUo) {
	    this.linesUo.add(lineUo);
		lineUo.setProposition(this);
	}

    public void removeLineUo(LineUo lineUo) {
        this.linesUo.remove(lineUo);
        lineUo.setProposition(null);
    }

	public void addLineTrafficRevenue(LineTrafficRevenue lineTrafficRevenue) {
	    this.lineTrafficRevenues.add(lineTrafficRevenue);
	    lineTrafficRevenue.setProposition(this);
    }

    public void removeLineTrafficRevenue(LineTrafficRevenue lineTrafficRevenue) {
        this.lineTrafficRevenues.remove(lineTrafficRevenue);
        lineTrafficRevenue.setProposition(null);
    }

	public void addLineData(LineData lineData) {
        this.linesData.add(lineData);
		lineData.setProposition(this);
	}

    public void removeLineData(LineData lineData) {
        this.linesData.remove(lineData);
        lineData.setProposition(null);
    }

	public void addLine(Line line) {
		this.lines.add(line);
	}

	public void setComplete(boolean complete) {
		this.complete = complete;
		updateStatus();
	}

	/**
	 * Mettre à jour le status de cette proposition suite à le changement de complétude dans certains cas
	 */
	public void updateStatus() {
		if (passToCompletedCondition()) {
			passToStatus(PropositionStatus.COMPLETED);
		}
		else if (passToInitializedCondition()) {
			passToStatus(PropositionStatus.INITIALIZED);
		}
	}

	/**
	 * Savoir si une proposition est considérée comme complète.
	 */
	protected boolean isConsideredComplete() {
		return complete;
	}

	/**
	 * Opération à effectuer pour passer cette proposition en status donné
	 */
	protected void passToStatus(PropositionStatus status) {
		this.status = status;
	}

	private boolean passToCompletedCondition() {
		return isConsideredComplete() && status == PropositionStatus.INITIALIZED;
	}

	private boolean passToInitializedCondition() {
		return !isConsideredComplete() && this.status == PropositionStatus.COMPLETED;
	}

	@JsonProperty("fullName")
	@JsonView({ JsonViews.Proposition.class })
	public String getFullName() {
		return getName();
	}

	@JsonIgnore
	public boolean isAllRegularLine() {
		// Ignorer lignes retirees
		return getNonRetireeLineStream().allMatch(l -> l.offerTypeIs(SubLine.OfferTypeEnum.REGULAR_OFFER));
	}

	@JsonIgnore
	public boolean isAllTad() {
		// Ignorer les lignes retirees
		return getNonRetireeLineStream().allMatch(l -> l.offerTypeIs(SubLine.OfferTypeEnum.TAD_OFFER));
	}

	@JsonIgnore
	public boolean hasTad() {
		return getNonRetireeLineStream()
				.anyMatch(l -> l.offerTypeIs(SubLine.OfferTypeEnum.TAD_OFFER) || l.offerTypeIs(SubLine.OfferTypeEnum.MIXT_OFFER));
	}

    @JsonIgnore
    public boolean hasLineType(LineType lineType) {
        return getNonRetireeLineStream().anyMatch(l -> l.getLineType() == lineType);
    }

    @JsonIgnore
	public boolean hasCss() {
		return hasLineType(LineType.CSS);
	}

	/**
	 * Tester si cette proposition est saisissable pour l'annee donnee
	 *
	 * @param yearNumber
	 * @return
	 */
	@JsonIgnore
	public boolean isYearUpdatable(Integer yearNumber) {
	    for (PropositionYear propositionYear: getPropositionYears()) {
           if (propositionYear.getNumber() == yearNumber) {
                return propositionYear.isUpdatable();
            }
        }

	    return false;
	}

	/**
	 * Pour une annee donnee, si cette proposition est saisissable, retourner cette prposition, sinon remonter dans la hierarchie de reference
	 * jusqu'a trouver une proposition saisissable ou InitialContractualProposition.
	 *
	 * Autrement dit, pour l'annee donnee, retourner la proposition qui est la source des donnees de cette proposition.
	 *
	 * => On considere qu'une InitialContractualProposition est toujours une source
	 *
	 * @param yearNumber
	 * @return
	 */
	@JsonIgnore
	public Optional<Proposition> getPropositionDeSourceForYear(Integer yearNumber) {
		if (isYearUpdatable(yearNumber)) {
			return Optional.of(this);
		}

		// Year non uptable
		Proposition refProposition = getReferenceContractualProposition();
		if (refProposition != null) {
			return refProposition.getPropositionDeSourceForYear(yearNumber);
		}
		return Optional.empty();
	}

	/** Enlever la ligne <code>line</code> de cette proposition */
	public void removeLine(Line line) {
		lines.remove(line);
	}

	/** Retourner les lignes non retirees de cette proposition */
	@JsonIgnore
	public Set<Line> getNonRetireeLines() {
		return getNonRetireeLineStream()
				.collect(Collectors.toSet());
	}

	private Stream<Line> getNonRetireeLineStream() {
		return lines.stream().filter(l -> !l.isRetiree());
	}

	// Trivial getters and setters

	public boolean isAnonymized() {
		return anonymized;
	}

	public void setAnonymized(boolean anonymized) {
		this.anonymized = anonymized;
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

    public boolean isImported() {
        return false;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Line> getLines() {
		return lines;
	}

	public void setLines(Set<Line> lines) {
		this.lines = lines;
	}

	public Set<LineData> getLinesData() {
		return linesData;
	}

	public void setLinesData(Set<LineData> linesData) {
		this.linesData = linesData;
	}

	public Set<LineTrafficRevenue> getLineTrafficRevenues() {
		return lineTrafficRevenues;
	}

	public void setLineTrafficRevenues(Set<LineTrafficRevenue> lineTrafficRevenues) {
		this.lineTrafficRevenues = lineTrafficRevenues;
	}

	public Set<LineUo> getLinesUo() {
		return linesUo;
	}

	public void setLinesUo(Set<LineUo> linesUo) {
		this.linesUo = linesUo;
	}

	public Set<VehicleType> getVehicleTypes() {
		return vehicleTypes;
	}

	public void setVehicleTypes(Set<VehicleType> vehicleTypes) {
		this.vehicleTypes = vehicleTypes;
	}

	public Set<Vehicle> getVehicles() {
		return vehicles;
	}

	public void setVehicles(Set<Vehicle> vehicles) {
		this.vehicles = vehicles;
	}

	public Set<SecurityEquipmentType> getSecurityEquipmentTypes() {
		return securityEquipmentTypes;
	}

	public void setSecurityEquipmentTypes(Set<SecurityEquipmentType> securityEquipmentTypes) {
		this.securityEquipmentTypes = securityEquipmentTypes;
	}

	public Set<SecurityEquipment> getSecurityEquipments() {
		return securityEquipments;
	}

	public void setSecurityEquipments(Set<SecurityEquipment> securityEquipments) {
		this.securityEquipments = securityEquipments;
	}

	public Set<SecurityEquipmentSummary> getSecurityEquipmentSummaries() {
		return securityEquipmentSummaries;
	}

	public void setSecurityEquipmentSummaries(Set<SecurityEquipmentSummary> securityEquipmentSummaries) {
		this.securityEquipmentSummaries = securityEquipmentSummaries;
	}

	public Set<SAEIVEquipmentType> getSaeivEquipmentTypes() {
		return saeivEquipmentTypes;
	}

	public void setSaeivEquipmentTypes(Set<SAEIVEquipmentType> saeivEquipmentTypes) {
		this.saeivEquipmentTypes = saeivEquipmentTypes;
	}

	public Set<SAEIVEquipment> getSaeivEquipments() {
		return saeivEquipments;
	}

	public void setSaeivEquipments(Set<SAEIVEquipment> saeivEquipments) {
		this.saeivEquipments = saeivEquipments;
	}

	public Set<TicketsEquipmentType> getTicketsEquipmentTypes() {
		return ticketsEquipmentTypes;
	}

	public void setTicketsEquipmentTypes(Set<TicketsEquipmentType> ticketsEquipmentTypes) {
		this.ticketsEquipmentTypes = ticketsEquipmentTypes;
	}

	public Set<TicketsEquipment> getTicketsEquipments() {
		return ticketsEquipments;
	}

	public void setTicketsEquipments(Set<TicketsEquipment> ticketsEquipments) {
		this.ticketsEquipments = ticketsEquipments;
	}

	public Set<TicketsEquipmentSummary> getTicketsEquipmentSummaries() {
		return ticketsEquipmentSummaries;
	}

	public void setTicketsEquipmentSummaries(Set<TicketsEquipmentSummary> ticketsEquipmentSummaries) {
		this.ticketsEquipmentSummaries = ticketsEquipmentSummaries;
	}

	public Set<SAEIVEquipmentSummary> getSaeivEquipmentSummaries() {
		return saeivEquipmentSummaries;
	}

	public void setSaeivEquipmentSummaries(Set<SAEIVEquipmentSummary> saeivEquipmentSummaries) {
		this.saeivEquipmentSummaries = saeivEquipmentSummaries;
	}

	public Set<ComplementaryRevenue> getComplementaryRevenues() {
		return complementaryRevenues;
	}

	public void setComplementaryRevenues(Set<ComplementaryRevenue> complementaryRevenues) {
		this.complementaryRevenues = complementaryRevenues;
	}

	public Set<VehicleSummary> getVehicleSummaries() {
		return vehicleSummaries;
	}

	public void setVehicleSummaries(Set<VehicleSummary> vehicleSummaries) {
		this.vehicleSummaries = vehicleSummaries;
	}

	public Set<Infrastructure> getInfrastructures() {
		return infrastructures;
	}

	public void setInfrastructures(Set<Infrastructure> infrastructures) {
		this.infrastructures = infrastructures;
	}

	public Set<InfrastructureSummary> getInfrastructureSummaries() {
		return infrastructureSummaries;
	}

	public void setInfrastructureSummaries(Set<InfrastructureSummary> infrastructureSummaries) {
		this.infrastructureSummaries = infrastructureSummaries;
	}

	public Set<SiteMaintenance> getSiteMaintenances() {
		return siteMaintenances;
	}

	public void setSiteMaintenances(Set<SiteMaintenance> siteMaintenances) {
		this.siteMaintenances = siteMaintenances;
	}

	public PropositionStatus getStatus() {
		return status;
	}

	public void setStatus(PropositionStatus status) {
		this.status = status;
	}

	public boolean isComplete() {
		return complete;
	}

    public Set<MarginalCost> getMarginalCosts() {
        return marginalCosts;
    }

    public void setMarginalCosts(Set<MarginalCost> marginalCosts) {
        this.marginalCosts = marginalCosts;
    }

	@JsonIgnore
	public List<ContractualYear> getEffectiveYears() {
		return getYears();
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@JsonIgnore
	public Proposition getReferenceContractualProposition() {
		return null;
	}

	public BigDecimal getImportedData(String key) {
		return null;
	}

	public DateTime getContractualDate() {
		return contractualDate;
	}

	public void setContractualDate(DateTime contractualDate) {
		this.contractualDate = contractualDate;
	}

	public void setNewContractualized(Boolean newContractualized) {
		this.newContractualized = newContractualized;
	}
}
