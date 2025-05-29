package co.edu.uco.easy.victusresidencias.victus_api.controller;

import co.edu.uco.easy.victusresidencias.victus_api.controller.response.GenerateResponse;
import co.edu.uco.easy.victusresidencias.victus_api.controller.response.concrete.AdministratorResponse;
import co.edu.uco.easy.victusresidencias.victus_api.controller.response.concrete.CityResponse;
import co.edu.uco.easy.victusresidencias.victus_api.controller.response.concrete.GenericResponse;
import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.exceptions.BusinessLogicVictusResidenciasException;
import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.exceptions.UcoApplicationException;
import co.edu.uco.easy.victusresidencias.victus_api.dao.DAOFactory;
import co.edu.uco.easy.victusresidencias.victus_api.dao.enums.DAOSource;
import co.edu.uco.easy.victusresidencias.victus_api.dao.impl.postgresql.PostgreSqlDAOFactory;
import co.edu.uco.easy.victusresidencias.victus_api.entity.AdministratorEntity;
import co.edu.uco.easy.victusresidencias.victus_api.entity.CityEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//@CrossOrigin(origins = "https://tangerine-profiterole-824fd8.netlify.app")
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/administradores")
public class AdministradorControlador {
    private static final String NAMEclassSingular = "Administrador";
    private static final String NAMEclassPlural = "Administradores";
    private final PostgreSqlDAOFactory daoFactory;

    public AdministradorControlador() {this.daoFactory = new PostgreSqlDAOFactory();}

    @GetMapping("/dummy")
    public AdministratorEntity getDummy(){return AdministratorEntity.create();}

    @GetMapping("/todos")
    public ResponseEntity<AdministratorResponse> retrieveAll() {
        var response = new AdministratorResponse();
        var messages = new ArrayList<String>();

        try {
            var entities = daoFactory.getAdministratorDAO().findAll();
            response.setData(entities);
            messages.add(String.format("Los %s fueron consultados satisfactoriamente.",NAMEclassPlural));
            response.setMessages(messages);
            var factory = DAOFactory.getFactory(DAOSource.POSTGRESQL);
            factory.closeConnection();
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            messages.add(String.format("Error al consultar los %s. Por favor intente nuevamente.",NAMEclassPlural));
            response.setMessages(messages);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<AdministratorResponse> retrieveById(@PathVariable UUID id) {
        var response = new AdministratorResponse();
        var messages = new ArrayList<String>();

        try {
            var entity = daoFactory.getAdministratorDAO().fingByID(id);

            if (entity == null) {
                messages.add(String.format("No se encontró un %s con el ID especificado.",NAMEclassSingular));
                response.setMessages(messages);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            response.setData(List.of(entity));
            messages.add(String.format("El %s fue consultado satisfactoriamente.",NAMEclassSingular));
            response.setMessages(messages);
            var factory = DAOFactory.getFactory(DAOSource.POSTGRESQL);
            factory.closeConnection();
            return new GenerateResponse<AdministratorResponse>().generateSuccessResponseWithData(response);

        } catch (Exception e) {
            e.printStackTrace();
            messages.add(String.format("Error al consultar el %s. Por favor intente nuevamente.",NAMEclassSingular));
            response.setMessages(messages);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<GenericResponse> create(@RequestBody AdministratorEntity administrador) {
        var messages = new ArrayList<String>();

        try {
            var administradorEntityFilter = new AdministratorEntity();
            administradorEntityFilter.setName(administrador.getName());

            // Buscar en la base de datos utilizando el filtro
            boolean administradorExists = !daoFactory.getAdministratorDAO().findByFilter(administradorEntityFilter).isEmpty();
            boolean administradorIsEmpty = administradorEntityFilter.getName().isEmpty() || administradorEntityFilter.getName() == null;
            // Lanzar excepción si se encuentra un dato con el mismo nombre
            if (administradorIsEmpty) {
                String userMessage = String.format("El %s es requerido para poder registrar la información.",NAMEclassSingular);
                String technicalMessage = String.format("El %s en la clase %s llegó nulo o vacío.",NAMEclassSingular,AdministratorEntity.class.getSimpleName());
                throw BusinessLogicVictusResidenciasException.crear(userMessage, technicalMessage);
            }else if (administradorExists) {
                String userMessage = String.format("El %s ya existe",NAMEclassSingular);
                String technicalMessage = String.format("El %s con el nombre ",NAMEclassSingular) + administrador.getName() + "' ya existe en la base de datos.";

                throw BusinessLogicVictusResidenciasException.crear(userMessage, technicalMessage);
            }
            // Si no existe, procede a crear el país
            daoFactory.getAdministratorDAO().create(administrador);
            messages.add(String.format("El %s se registró de forma satisfactoria.",NAMEclassSingular));
            var factory = DAOFactory.getFactory(DAOSource.POSTGRESQL);
            factory.closeConnection();
            return GenerateResponse.generateSuccessResponse(messages);

        } catch (UcoApplicationException exception) {
            messages.add(exception.getUserMessage());
            exception.printStackTrace();
            return GenerateResponse.generateFailedResponse(messages);

        } catch (Exception exception) {
            messages.add(String.format("Se ha presentado un problema inesperado al registrar el %s.",NAMEclassSingular));
            exception.printStackTrace();
            return GenerateResponse.generateFailedResponse(messages);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdministratorResponse> update(@PathVariable UUID id, @RequestBody AdministratorEntity administrador) {
        var responseWithData = new AdministratorResponse();
        var messages = new ArrayList<String>();

        try {
            // Paso 1: Obtener el dato actual de la base de datos
            AdministratorEntity existingAdministradorEntity = daoFactory.getAdministratorDAO().fingByID(id);

            if (existingAdministradorEntity == null) {
                messages.add(String.format("No se encontró un %s con el ID especificado.",NAMEclassSingular));
                responseWithData.setMessages(messages);
                return new ResponseEntity<>(responseWithData, HttpStatus.NOT_FOUND);
            }
            // Paso 2: Fusionar datos nuevos con los existentes
            if (administrador.getName() != "") existingAdministradorEntity.setName(administrador.getName());
            if (administrador.getLastName() != "") existingAdministradorEntity.setLastName(administrador.getLastName());
            if (administrador.getIdType() != "") existingAdministradorEntity.setIdType(administrador.getIdType());
            if (administrador.getIdNumber() != "") existingAdministradorEntity.setIdNumber(administrador.getIdNumber());
            if (administrador.getContactNumber() != "") existingAdministradorEntity.setContactNumber(administrador.getContactNumber());
            if (administrador.getEmail() != "") existingAdministradorEntity.setEmail(administrador.getEmail());
            if (administrador.getPassword() != "") existingAdministradorEntity.setPassword(administrador.getPassword());

            // Paso 3: Guardar los cambios en la base de datos
            daoFactory.getAdministratorDAO().update(existingAdministradorEntity);

            // Mensaje de éxito
            messages.add(String.format("El %s ",NAMEclassSingular) + existingAdministradorEntity.getName() + " se actualizó correctamente.");
            responseWithData.setData(List.of(existingAdministradorEntity));
            responseWithData.setMessages(messages);
            var factory = DAOFactory.getFactory(DAOSource.POSTGRESQL);
            factory.closeConnection();
            return new ResponseEntity<>(responseWithData, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            messages.add(String.format("Error al actualizar el %s. Por favor intente nuevamente.",NAMEclassSingular));
            responseWithData.setMessages(messages);
            return new ResponseEntity<>(responseWithData, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GenericResponse> delete(@PathVariable UUID id) {
        var messages = new ArrayList<String>();
        AdministratorEntity existingAdministradorEntity = daoFactory.getAdministratorDAO().fingByID(id);


        try {
            if (existingAdministradorEntity == null) {
                messages.add(String.format("No se encontró un %s con el ID especificado.",NAMEclassSingular));
                return new GenerateResponse<GenericResponse>().generateFailedResponse(messages);
            }else if (id == null) {
                var userMessage = String.format("El ID del %S es requerido para poder eliminar la información.",NAMEclassSingular);
                var technicalMessage = String.format("El ID del %s en la clase CountryEntity llegó nulo o vacío.",NAMEclassSingular);
                throw BusinessLogicVictusResidenciasException.crear(userMessage, technicalMessage);
            }
            daoFactory.getAdministratorDAO().delete(id);
            messages.add(String.format("El %s se eliminó de manera satisfactoria.",NAMEclassSingular));
            var factory = DAOFactory.getFactory(DAOSource.POSTGRESQL);
            factory.closeConnection();
            return GenerateResponse.generateSuccessResponse(messages);

        } catch (Exception e) {
            e.printStackTrace();
            messages.add(String.format("Error al eliminar el %s. Por favor intente nuevamente.",NAMEclassSingular));
            return GenerateResponse.generateFailedResponse(messages);
        }
    }
}
