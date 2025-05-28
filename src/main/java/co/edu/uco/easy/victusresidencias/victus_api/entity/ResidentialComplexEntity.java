package co.edu.uco.easy.victusresidencias.victus_api.entity;

import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.helpers.*;

import java.util.UUID;

public class ResidentialComplexEntity extends DomainEntity {

    private String name;
    private String address;
    private CityEntity city;
    private String contactReception;
    private String description;
    private AdministratorEntity administrator;
    private PorteroEntity portero;

    public ResidentialComplexEntity() {
        super(UUIDHelper.getDefault());
        setName(TextHelper.EMPTY);
        setAdministrator(new AdministratorEntity());
        setCity(new CityEntity());
        setContactReception(TextHelper.EMPTY);
        setAddress(TextHelper.EMPTY);
        setDescription(TextHelper.EMPTY);
    }

    public static final ResidentialComplexEntity create(){return new ResidentialComplexEntity();}

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

    public CityEntity getCity() {
        return city;
    }

    public void setCity(final CityEntity city) {
        this.city = ObjectHelper.getDefault(city, new CityEntity());
    }

    public PorteroEntity getPortero() {
        return portero;
    }
    public void setPortero(final PorteroEntity portero) {
        this.portero = ObjectHelper.getDefault(portero, new PorteroEntity());
    }
    public AdministratorEntity getAdministrator() {
        return administrator;
    }

    public void setAdministrator(final AdministratorEntity administrator) {
        this.administrator = ObjectHelper.getDefault(administrator, new AdministratorEntity());
    }

    public String getAddress() {
        return address; // Corregido a minúscula
    }

    public void setAddress(String address) {
        this.address = address; // Corregido a minúscula
    }

    public String getContactReception() { // Cambiado a String
        return contactReception; // Cambiado a String
    }

    public void setContactReception(String contactReception) { // Cambiado a String
        this.contactReception = contactReception; // Cambiado a String
    }

    public String getDescription() { // Corregido a getDescription
        return description; // Corregido a description
    }

    public void setDescription(String description) { // Corregido a setDescription
        this.description = description; // Corregido a description
    }
}
