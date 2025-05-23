package co.edu.uco.easy.victusresidencias.victus_api.entity;

import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.helpers.*;

import java.util.UUID;

public class PropertyEntity extends DomainEntity {
	
	private String propertyType;
	private int propertyNumber;

	public PropertyEntity() {
		super(UUIDHelper.getDefault());
		setPropertyType(TextHelper.EMPTY);
		setPropertyNumber(NumericHelper.CERO);
	}
	
	public String getPropertyType() {
		return propertyType;
	}

	public void setPropertyType(String propertyType) {
		this.propertyType = TextHelper.applyTrim(propertyType);
	}
	
	
	public int getPropertyNumber() {
		return propertyNumber;
	}

	public void setPropertyNumber(int propertyNumber) {
		this.propertyNumber = propertyNumber;
	}

	@Override
	public void setId(final UUID id) {
		super.setId(id);
	}
	@Override
	public UUID getId() {
		return super.getId();
	}



	
}