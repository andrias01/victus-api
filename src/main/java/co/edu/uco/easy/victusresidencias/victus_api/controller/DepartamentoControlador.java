package co.edu.uco.easy.victusresidencias.victus_api.controller;

import co.edu.uco.easy.victusresidencias.victus_api.controller.response.GenerateResponse;
import co.edu.uco.easy.victusresidencias.victus_api.controller.response.concrete.CountryResponse;
import co.edu.uco.easy.victusresidencias.victus_api.controller.response.concrete.GenericResponse;
import co.edu.uco.easy.victusresidencias.victus_api.controller.response.concrete.StateResponse;
import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.exceptions.BusinessLogicVictusResidenciasException;
import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.exceptions.UcoApplicationException;
import co.edu.uco.easy.victusresidencias.victus_api.dao.DAOFactory;
import co.edu.uco.easy.victusresidencias.victus_api.dao.enums.DAOSource;
import co.edu.uco.easy.victusresidencias.victus_api.dao.impl.postgresql.PostgreSqlDAOFactory;
import co.edu.uco.easy.victusresidencias.victus_api.entity.CountryEntity;
import co.edu.uco.easy.victusresidencias.victus_api.entity.ResidentEntity;
import co.edu.uco.easy.victusresidencias.victus_api.entity.StateEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//@CrossOrigin(origins = "https://tangerine-profiterole-824fd8.netlify.app")
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/departamentos")
public class DepartamentoControlador {
    private static final String NAMEclassSingular = "Departamento";
    private static final String NAMEclassPlural = "Departamentos";
    private final PostgreSqlDAOFactory daoFactory;

    public DepartamentoControlador() {this.daoFactory = new PostgreSqlDAOFactory();}

    @GetMapping("/dummy")
    public StateEntity getDummy(){return StateEntity.create();}

    @GetMapping("/todos")
    public ResponseEntity<StateResponse> retrieveAll() {
        var response = new StateResponse();
        var messages = new ArrayList<String>();

        try {
            var entities = daoFactory.getStateDAO().findAll();
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
    public ResponseEntity<StateResponse> retrieveById(@PathVariable UUID id) {
        var response = new StateResponse();
        var messages = new ArrayList<String>();

        try {
            var entity = daoFactory.getStateDAO().fingByID(id);

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
            return new GenerateResponse<StateResponse>().generateSuccessResponseWithData(response);

        } catch (Exception e) {
            e.printStackTrace();
            messages.add(String.format("Error al consultar el %s. Por favor intente nuevamente.",NAMEclassSingular));
            response.setMessages(messages);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<GenericResponse> create(@RequestBody StateEntity departamento) {
        var messages = new ArrayList<String>();

        try {
            var stateEntityFilter = new StateEntity();
            stateEntityFilter.setName(departamento.getName());

            // Buscar en la base de datos utilizando el filtro
            boolean stateExists = !daoFactory.getStateDAO().findByFilter(stateEntityFilter).isEmpty();
            boolean stateIsEmpty = stateEntityFilter.getName().isEmpty() || stateEntityFilter.getName() == null;
            // Lanzar excepción si se encuentra un dato con el mismo nombre
            if (stateIsEmpty) {
                String userMessage = String.format("El %s es requerido",NAMEclassSingular);
                String technicalMessage = String.format("El %s con el nombre ",NAMEclassSingular) + departamento.getName() + "' es requerido en la base de datos.";

                throw BusinessLogicVictusResidenciasException.crear(userMessage, technicalMessage);
            } else if (stateExists) {
                String userMessage = String.format("El %s ya existe",NAMEclassSingular);
                String technicalMessage = String.format("El %s con el nombre ",NAMEclassSingular) + departamento.getName() + "' ya existe en la base de datos.";

                throw BusinessLogicVictusResidenciasException.crear(userMessage, technicalMessage);
            }
            // Si no existe, procede a crear el país
            daoFactory.getStateDAO().create(departamento);
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
    public ResponseEntity<StateResponse> update(@PathVariable UUID id, @RequestBody StateEntity departamento) {
        var responseWithData = new StateResponse();
        var messages = new ArrayList<String>();

        try {
            // Paso 1: Obtener el dato actual de la base de datos
            StateEntity existingDepartamentoEntity = daoFactory.getStateDAO().fingByID(id);

            if (existingDepartamentoEntity == null) {
                messages.add(String.format("No se encontró un %s con el ID especificado.",NAMEclassSingular));
                responseWithData.setMessages(messages);
                return new ResponseEntity<>(responseWithData, HttpStatus.NOT_FOUND);
            }
            // Paso 2: Fusionar datos nuevos con los existentes
            if (departamento.getName() != "") existingDepartamentoEntity.setName(departamento.getName());

            // Paso 3: Guardar los cambios en la base de datos
            daoFactory.getStateDAO().update(existingDepartamentoEntity);

            // Mensaje de éxito
            messages.add(String.format("El %s ",NAMEclassSingular) + existingDepartamentoEntity.getName() + " se actualizó correctamente.");
            responseWithData.setData(List.of(existingDepartamentoEntity));
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
        StateEntity existingDepartamentoEntity = daoFactory.getStateDAO().fingByID(id);

        try {
            if (id == null) {
                var userMessage = String.format("El ID del %S es requerido para poder eliminar la información.",NAMEclassSingular);
                var technicalMessage = String.format("El ID del %s en la clase CountryEntity llegó nulo o vacío.",NAMEclassSingular);
                throw BusinessLogicVictusResidenciasException.crear(userMessage, technicalMessage);
            } else if (existingDepartamentoEntity == null) {
                messages.add(String.format("No se encontró un %s con el ID especificado.",NAMEclassSingular));
                return GenerateResponse.generateFailedResponse(messages);
            }
            daoFactory.getStateDAO().delete(id);
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
