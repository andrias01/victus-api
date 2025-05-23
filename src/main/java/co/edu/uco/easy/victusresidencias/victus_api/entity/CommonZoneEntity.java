package co.edu.uco.easy.victusresidencias.victus_api.entity;

import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.helpers.*;

import java.util.UUID;

public class CommonZoneEntity extends DomainEntity {
	
	private String name;
	private String description;
	private int peopleCapacity;
	private int usageTime;
	private String usingTimeUnit;
	private String rule;
	private ResidentialComplexEntity residentialComplex;


	public CommonZoneEntity() {
		super(UUIDHelper.getDefault());
		setName(TextHelper.EMPTY);
		setDescription(TextHelper.EMPTY);
		setPeopleCapacity(NumericHelper.CERO);
		setUsingTime(NumericHelper.CERO);
		setUsingTimeUnit(TextHelper.EMPTY);
		setRule(TextHelper.EMPTY);
		setResidentialComplex(new ResidentialComplexEntity());
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = TextHelper.applyTrim(name);
	}
	
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getPeopleCapacity() {
		return peopleCapacity;
	}

	public void setPeopleCapacity(int peopleCapacity) {
		this.peopleCapacity = peopleCapacity;
	}

	public int getUsingTime() {
		return usageTime;
	}

	public void setUsingTime(int usingTime) {
		this.usageTime = usingTime;
	}

	public String getUsingTimeUnit() {
		return usingTimeUnit;
	}

	public void setUsingTimeUnit(String usingTimeUnit) {
		this.usingTimeUnit = usingTimeUnit;
	}

	public String getRule() {
		return rule;
	}

	public void setRule(String rule) {
		this.rule = rule;
	}

	@Override
	public void setId(final UUID id) {
		super.setId(id);
	}
	@Override
	public UUID getId() {
		return super.getId();
	}

	public ResidentialComplexEntity getResidentialComplex() {
		return residentialComplex;
	}

	public void setResidentialComplex(final ResidentialComplexEntity residentialComplexEntity) {
		this.residentialComplex = ObjectHelper.getDefault(residentialComplexEntity, new ResidentialComplexEntity());
	}
}