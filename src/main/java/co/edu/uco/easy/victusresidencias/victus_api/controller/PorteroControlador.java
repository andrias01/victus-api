package co.edu.uco.easy.victusresidencias.victus_api.controller;

import co.edu.uco.easy.victusresidencias.victus_api.controller.response.GenerateResponse;
import co.edu.uco.easy.victusresidencias.victus_api.controller.response.concrete.CountryResponse;
import co.edu.uco.easy.victusresidencias.victus_api.controller.response.concrete.GenericResponse;
import co.edu.uco.easy.victusresidencias.victus_api.controller.response.concrete.PorteroResponse;
import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.exceptions.BusinessLogicVictusResidenciasException;
import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.exceptions.UcoApplicationException;
import co.edu.uco.easy.victusresidencias.victus_api.dao.DAOFactory;
import co.edu.uco.easy.victusresidencias.victus_api.dao.enums.DAOSource;
import co.edu.uco.easy.victusresidencias.victus_api.dao.impl.postgresql.PostgreSqlDAOFactory;
import co.edu.uco.easy.victusresidencias.victus_api.entity.CountryEntity;
import co.edu.uco.easy.victusresidencias.victus_api.entity.PorteroEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "https://tangerine-profiterole-824fd8.netlify.app")
@RestController
@RequestMapping("/api/v1/porteros")
public class PorteroControlador {
    private static final String NAMEclassSingular = "Portero";
    private static final String NAMEclassPlural = "Porteros";

    private final PostgreSqlDAOFactory daoFactory;

    @Autowired
    public PorteroControlador(){this.daoFactory = new PostgreSqlDAOFactory();}

    @GetMapping("/dummy")
    public PorteroEntity getDummy() {
        return PorteroEntity.create();
    }

    @GetMapping("/todos")
    public ResponseEntity<PorteroResponse> retrieveAll() {
        var response = new PorteroResponse();
        var messages = new ArrayList<String>();

        try {
            var entities = daoFactory.getPorteroDAO().findAll();
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
    public ResponseEntity<PorteroResponse> retrieveById(@PathVariable UUID id) {
        var response = new PorteroResponse();
        var messages = new ArrayList<String>();

        try {
            var entity = daoFactory.getPorteroDAO().fingByID(id);

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
            return new GenerateResponse<PorteroResponse>().generateSuccessResponseWithData(response);

        } catch (Exception e) {
            e.printStackTrace();
            messages.add(String.format("Error al consultar el %s. Por favor intente nuevamente.",NAMEclassSingular));
            response.setMessages(messages);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<GenericResponse> create(@RequestBody PorteroEntity portero) {
        var messages = new ArrayList<String>();

        try {
            var porteroEntityFilter = new PorteroEntity();
            porteroEntityFilter.setNombre(portero.getNombre());

            // Buscar en la base de datos utilizando el filtro
            boolean porteroExists = !daoFactory.getPorteroDAO().findByFilter(porteroEntityFilter).isEmpty();
            boolean porteroIsEmpty = portero.getNombre() == null || portero.getNombre().isEmpty();

            // Lanzar excepción si se encuentra un país con el mismo nombre, nullo o vacío
            if (porteroIsEmpty) {
                String userMessage = String.format("El %s no puede estar vacío",NAMEclassSingular);
                String technicalMessage = String.format("El %s con el nombre ",NAMEclassSingular) + portero.getNombre() + "' no puede estar vacío para crearlo en la base de datos.";
                throw BusinessLogicVictusResidenciasException.crear(userMessage, technicalMessage);
            } else if (porteroExists) {
                String userMessage = String.format("El %s ya existe",NAMEclassSingular);
                String technicalMessage = String.format("El %s con el nombre ",NAMEclassSingular) + portero.getNombre() + "' ya existe en la base de datos.";
                throw BusinessLogicVictusResidenciasException.crear(userMessage, technicalMessage);
            }
            // Si no existe, procede a crear el país
            daoFactory.getPorteroDAO().create(portero);
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
    public ResponseEntity<PorteroResponse> update(@PathVariable UUID id, @RequestBody PorteroEntity portero) {
        var responseWithData = new PorteroResponse();
        var messages = new ArrayList<String>();

        try {
            // Paso 1: Obtener el dato actual de la base de datos
            PorteroEntity existingPorteroEntity = daoFactory.getPorteroDAO().fingByID(id);

            if (existingPorteroEntity == null) {
                messages.add(String.format("No se encontró un %s con el ID especificado.",NAMEclassSingular));
                responseWithData.setMessages(messages);
                return new ResponseEntity<>(responseWithData, HttpStatus.NOT_FOUND);
            }
            // Paso 2: Fusionar datos nuevos con los existentes
            if (portero.getNombre() != "") existingPorteroEntity.setNombre(portero.getNombre());
            if (portero.getApellido() != "") existingPorteroEntity.setApellido(portero.getApellido());
            if (portero.getTipo_documento() != "") existingPorteroEntity.setTipo_documento(portero.getTipo_documento());
            if (portero.getNumero_documento() != "") existingPorteroEntity.setNumero_documento(portero.getNumero_documento());
            if (portero.getNumero_contacto() != "") existingPorteroEntity.setNumero_contacto(portero.getNumero_contacto());
            if (portero.getEmail() != "") existingPorteroEntity.setEmail(portero.getEmail());
            if (portero.getContraseña() != "") existingPorteroEntity.setContraseña(portero.getContraseña());
            // Paso 3: Guardar los cambios en la base de datos
            daoFactory.getPorteroDAO().update(existingPorteroEntity);

            // Mensaje de éxito
            messages.add(String.format("El %s ",NAMEclassSingular) + existingPorteroEntity.getNombre() + " se actualizó correctamente.");
            responseWithData.setData(List.of(existingPorteroEntity));
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
        PorteroEntity existingPorteroEntity = daoFactory.getPorteroDAO().fingByID(id);

        try {
            if (id == null ) {
                var userMessage = String.format("El ID del %S es requerido para poder eliminar la información.",NAMEclassSingular);
                var technicalMessage = String.format("El ID del %s en la clase CountryEntity llegó nulo o vacío.",NAMEclassSingular);
                throw BusinessLogicVictusResidenciasException.crear(userMessage, technicalMessage);
            } else if (existingPorteroEntity == null) {
                messages.add(String.format("No se encontró un %s con el ID especificado.",NAMEclassSingular));
                return GenerateResponse.generateFailedResponse(messages);
            }
            daoFactory.getPorteroDAO().delete(id);
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
