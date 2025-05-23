package co.edu.uco.easy.victusresidencias.victus_api.entity;

import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.helpers.*;

import java.util.UUID;

public class AdministratorEntity extends DomainEntity {
	
	private String name;
	private String lastName;
	private String idType; // Cambiado de documentType a idType
	private String idNumber; // Cambiado de documentNumber a idNumber
	private String contactNumber;
	private String email;
	private String password;

	public AdministratorEntity() {
		super(UUIDHelper.getDefault());
		setName(TextHelper.EMPTY);
		setLastName(TextHelper.EMPTY);
		setIdType(TextHelper.EMPTY); // Cambiado de documentType a idType
		setIdNumber(TextHelper.EMPTY); // Cambiado de documentNumber a idNumber
		setContactNumber(TextHelper.EMPTY);
		setEmail(TextHelper.EMPTY);
		setPassword(TextHelper.EMPTY);
	}
	public static final AdministratorEntity create(){return new AdministratorEntity();}
	
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

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = TextHelper.applyTrim(lastName);
	}

	public String getIdType() { // Cambiado de getDocumentType a getIdType
		return idType;
	}

	public void setIdType(String idType) { // Cambiado de setDocumentType a setIdType
		this.idType = idType;
	}

	public String getIdNumber() { // Cambiado de getDocumentNumber a getIdNumber
		return idNumber;
	}

	public void setIdNumber(String idNumber) { // Cambiado de setDocumentNumber a setIdNumber
		this.idNumber = idNumber;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = TextHelper.applyTrim(email);
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
