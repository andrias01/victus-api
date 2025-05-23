package co.edu.uco.easy.victusresidencias.victus_api.entity;

import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.helpers.*;

import java.util.UUID;

public class CityEntity extends DomainEntity {
	
	private String name;
	private StateEntity state;


	public CityEntity() {
		super(UUIDHelper.getDefault());
		setName(TextHelper.EMPTY);
		setState(new StateEntity());
	}

	public static final CityEntity create(){return new CityEntity();}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = TextHelper.applyTrim(name);
	}
	@Override
	public void setId(final UUID id) {
		super.setId(id);
	}
	@Override
	public UUID getId() {
		return super.getId();
	}

	public StateEntity getState() {
		return state;
	}

	public void setState(final StateEntity state) {
		this.state = ObjectHelper.getDefault(state, new StateEntity());
	}
}