package co.edu.uco.easy.victusresidencias.victus_api.entity;

import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.helpers.*;

import java.time.LocalDate;
import java.util.UUID;

public class ResidentEntity extends DomainEntity {
    
    private String name;
    private String lastName;
    private String idType; // Cambiado de documentType a idType
    private String documentNumber; // Sigue siendo String
    private LocalDate birthDate;
    private String contactNumber; // Sigue siendo String
    private String password;
    private PropertyEntity property;

    public ResidentEntity() {
        super(UUIDHelper.getDefault());
        setName(TextHelper.EMPTY);
        setLastName(TextHelper.EMPTY);
        setIdType(TextHelper.EMPTY); // Cambiado a TextHelper.EMPTY
        setDocumentNumber(TextHelper.EMPTY); // Sigue siendo TextHelper.EMPTY
        setBirthDate(DateHelper.DEFAULT_DATE);
        setContactNumber(TextHelper.EMPTY); // Sigue siendo TextHelper.EMPTY
        setPassword(TextHelper.EMPTY);
        setProperty(new PropertyEntity());
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = TextHelper.applyTrim(name);
    }
    
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getIdType() { // Cambiado de documentType a idType
        return idType;
    }

    public void setIdType(String idType) { // Cambiado de documentType a idType
        this.idType = idType;
    }

    public String getDocumentNumber() { // Sigue siendo String
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) { // Sigue siendo String
        this.documentNumber = documentNumber;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getContactNumber() { // Sigue siendo String
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) { // Sigue siendo String
        this.contactNumber = contactNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public void setId(final UUID id) {
        super.setId(id);
    }
    
    @Override
    public UUID getId() {
        return super.getId();
    }
    
    public PropertyEntity getProperty() {
        return property;
    }

    public void setProperty(final PropertyEntity property) {
        this.property = ObjectHelper.getDefault(property, new PropertyEntity());
    }
}
