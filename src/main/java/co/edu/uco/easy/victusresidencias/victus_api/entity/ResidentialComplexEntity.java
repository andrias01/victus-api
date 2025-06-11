package co.edu.uco.easy.victusresidencias.victus_api.entity;

import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.helpers.*;


import java.util.UUID;

public class ResidentialComplexEntity extends DomainEntity {

    private String nombre;
    private String direccion;
    private String telefono_contacto;
    private String description;
    private String correo;
    private CityEntity ciudad;
    private PorteroEntity portero;
    private AdministratorEntity administrator;



    public ResidentialComplexEntity() {
        super(UUIDHelper.getDefault());
        setNombre(TextHelper.EMPTY);
        setAdministrator(new AdministratorEntity());
        setCiudad(new CityEntity());
        setPortero(new PorteroEntity());
        setTelefono_contacto(TextHelper.EMPTY);
        setDireccion(TextHelper.EMPTY);
        setDescription(TextHelper.EMPTY);
        setCorreo(TextHelper.EMPTY);
    }

    public static final ResidentialComplexEntity create(){return new ResidentialComplexEntity();}

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = TextHelper.applyTrim(nombre);
    }

    @Override
    public void setId(final UUID id) {
        super.setId(id);
    }

    @Override
    public UUID getId() {
        return super.getId();
    }

    public CityEntity getCiudad() {
        return ciudad;
    }

    public void setCiudad(final CityEntity ciudad) {
        this.ciudad = ObjectHelper.getDefault(ciudad, new CityEntity());
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

    public String getDireccion() {
        return direccion; // Corregido a minúscula
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion; // Corregido a minúscula
    }

    public String getTelefono_contacto() { // Cambiado a String
        return telefono_contacto; // Cambiado a String
    }

    public void setTelefono_contacto(String telefono_contacto) { // Cambiado a String
        this.telefono_contacto = telefono_contacto; // Cambiado a String
    }

    public String getCorreo() {return correo;  }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getDescription() { // Corregido a getDescription
        return description; // Corregido a description
    }

    public void setDescription(String description) { // Corregido a setDescription
        this.description = description; // Corregido a description
    }
}
