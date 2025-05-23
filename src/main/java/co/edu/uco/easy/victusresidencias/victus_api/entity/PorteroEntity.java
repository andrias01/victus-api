package co.edu.uco.easy.victusresidencias.victus_api.entity;

import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.helpers.TextHelper;
import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.helpers.UUIDHelper;

import java.util.UUID;

public class PorteroEntity extends DomainEntity {
    private String nombre;
    private String apellido;
    private String tipo_documento;
    private String numero_documento;
    private String numero_contacto;
    private String email;
    private String contraseña;

    public PorteroEntity(){
        super(UUIDHelper.getDefault());
        setNombre(TextHelper.EMPTY);
        setApellido(TextHelper.EMPTY);
        setTipo_documento(TextHelper.EMPTY);
        setNumero_documento(TextHelper.EMPTY);
        setNumero_contacto(TextHelper.EMPTY);
        setEmail(TextHelper.EMPTY);
        setContraseña(TextHelper.EMPTY);
    }

    @Override
    public void setId(final UUID id) {
        super.setId(id);
    }
    @Override
    public UUID getId() {
        return super.getId();
    }

    public static final PorteroEntity create(){return new PorteroEntity();}

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = TextHelper.applyTrim(nombre);
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = TextHelper.applyTrim(apellido);
    }

    public String getTipo_documento() {
        return tipo_documento;
    }

    public void setTipo_documento(String tipo_documento) {
        this.tipo_documento = TextHelper.applyTrim(tipo_documento);
    }

    public String getNumero_documento() {
        return numero_documento;
    }

    public void setNumero_documento(String numero_documento) {
        this.numero_documento = TextHelper.applyTrim(numero_documento);
    }

    public String getNumero_contacto() {
        return numero_contacto;
    }

    public void setNumero_contacto(String numero_contacto) {
        this.numero_contacto = TextHelper.applyTrim(numero_contacto);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = TextHelper.applyTrim(email);
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = TextHelper.applyTrim(contraseña);
    }
}
