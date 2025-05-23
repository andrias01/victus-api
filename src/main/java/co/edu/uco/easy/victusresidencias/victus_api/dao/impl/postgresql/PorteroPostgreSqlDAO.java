package co.edu.uco.easy.victusresidencias.victus_api.dao.impl.postgresql;

import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.exceptions.DataVictusResidenciasException;
import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.helpers.TextHelper;
import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.helpers.UUIDHelper;
import co.edu.uco.easy.victusresidencias.victus_api.dao.PorteroDAO;
import co.edu.uco.easy.victusresidencias.victus_api.dao.impl.sql.SqlDAO;
import co.edu.uco.easy.victusresidencias.victus_api.entity.CountryEntity;
import co.edu.uco.easy.victusresidencias.victus_api.entity.PorteroEntity;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PorteroPostgreSqlDAO extends SqlDAO implements PorteroDAO {
    private static final String FROM = "FROM portero ";
    private static final String SELECT = "SELECT id, nombre, apellido, tipo_documento, numero_documento, numero_contacto, email, contraseña ";
    private static final String DELETE = "DELETE FROM portero WHERE id = ?";
    private static final String UPDATE = "UPDATE portero SET nombre = ?, apellido = ?, tipo_documento = ?, numero_documento = ?, numero_contacto = ?, email = ?, contraseña = ? WHERE id = ?";
    private static final String NAMEclassSingular = "Portero";
    private static final String NAMEclassPlural = "Porteros";
    private static final String CREATEstatemente = "INSERT INTO portero(id, nombre, apellido, tipo_documento, numero_documento, numero_contacto, email, contraseña) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";

    public PorteroPostgreSqlDAO(Connection connection){
        super(connection);
    }

    @Override
    public PorteroEntity fingByID(UUID id) {
        var porteroEntityFilter = new PorteroEntity();
        porteroEntityFilter.setId(id);
        var result = findByFilter(porteroEntityFilter);
        return (result.isEmpty()) ? null : result.get(0); // Retorna null si no encuentra
    }

    @Override
    public List<PorteroEntity> findAll() {return findByFilter((new PorteroEntity()));}

    @Override
    public List<PorteroEntity> findByFilter(PorteroEntity filter) {
        final var statement = new StringBuilder(); //sentencia SQL
        final var parameters = new ArrayList<>();  // ?
        final var resultSelect = new ArrayList<PorteroEntity>(); //select para una lista entity
        var statementWasPrepared = false;	//sentencia fue preparada?

        createSelect(statement);
        createFrom(statement);
        createWhere(statement, filter, parameters);
        createOrderBy(statement);

        try (var preparedStatement = getConnection().prepareStatement(statement.toString())) {
            for (var arrayIndex = 0; arrayIndex < parameters.size(); arrayIndex++) {
                var statementIndex = arrayIndex + 1;
                preparedStatement.setObject(statementIndex, parameters.get(arrayIndex));
            }
            System.out.println("Sentencia preparada " + statement);
            statementWasPrepared = true;
            final var result = preparedStatement.executeQuery();
            while (result.next()) {
                var porteroEntityTmp = new PorteroEntity();
                porteroEntityTmp.setId(UUID.fromString(result.getString("id")));
                System.out.println("id que inserta a la lista " + UUID.fromString(result.getString("id")));
                porteroEntityTmp.setNombre(result.getString("nombre"));
                porteroEntityTmp.setApellido(result.getString("apellido"));
                porteroEntityTmp.setTipo_documento(result.getString("tipo_documento"));
                porteroEntityTmp.setNumero_documento(result.getString("numero_documento"));
                porteroEntityTmp.setNumero_contacto(result.getString("numero_contacto"));
                porteroEntityTmp.setEmail(result.getString("email"));
                porteroEntityTmp.setContraseña(result.getString("contraseña"));

                resultSelect.add(porteroEntityTmp);
            }
        } catch (final SQLException exception) {
            var userMessage = String.format("Se ha presentado un problema tratando de llevar a cabo la consulta de los %s.",NAMEclassPlural);
            var technicalMessage = statementWasPrepared ?
                    String.format("Problema ejecutando la consulta de los %s en la base de datos.",NAMEclassPlural) :
                    String.format("Problema preparando la consulta de los %s en la base de datos.",NAMEclassPlural);

            throw DataVictusResidenciasException.crear(userMessage, technicalMessage, exception);
        }

        return resultSelect;
    }
    private void createSelect(final StringBuilder statement) {statement.append(SELECT);}

    private void createFrom(final StringBuilder statement) {statement.append(FROM);}

    private void createWhere(final StringBuilder statement,
                             final PorteroEntity filter,
                             final List<Object> parameters) {
        if (!UUIDHelper.isDefault(filter.getId())) {
            System.out.println("Sentencia preparada con where " + filter.getId());
            statement.append("WHERE id = ? ");
            parameters.add(filter.getId());
        } else if (!TextHelper.isEmpty(filter.getNombre())) { // Condición para filtro de nombre
            statement.append("WHERE nombre = ? ");
            parameters.add(filter.getNombre());
        }//este if es para filtar por id o por nombre
    }

    private void createOrderBy(final StringBuilder statement) {statement.append("ORDER BY nombre ASC");}


    @Override
    public void create(PorteroEntity data) {
        PorteroEntity filter = new PorteroEntity();
        filter.setNombre(data.getNombre());
        if (!findByFilter(filter).isEmpty()) {
            throw DataVictusResidenciasException.crear(
                    String.format("El %s ya existe",NAMEclassSingular),
                    String.format("No se puede crear un %s con el nombre duplicado: ",NAMEclassSingular) + data.getNombre());
        }

        final StringBuilder statement = new StringBuilder();
        statement.append(CREATEstatemente);

        // Verificar si el ID es el UUID predeterminado, y si es así, generar uno nuevo.
        if (UUIDHelper.isDefault(data.getId())) {
            data.setId(UUIDHelper.generate()); // Genera un UUID único si es el valor predeterminado.
        }

        try (final var preparedStatement = getConnection().prepareStatement(statement.toString())) {
            preparedStatement.setObject(1, data.getId());
            preparedStatement.setString(2, data.getNombre());
            preparedStatement.setString(3,data.getApellido());
            preparedStatement.setString(4,data.getTipo_documento());
            preparedStatement.setString(5,data.getNumero_documento());
            preparedStatement.setString(6,data.getNumero_contacto());
            preparedStatement.setString(7,data.getEmail());
            preparedStatement.setString(8,data.getContraseña());
            System.out.println("Se creó el portero con el nombre " + data.getEmail() + " exitosamente");

            preparedStatement.executeUpdate();
            System.out.println("Se creó el portero con el nombre " + data.getNombre() + " exitosamente");

        } catch (final SQLException exception) {
            var userMessage = String.format("Se ha presentado un problema tratando de llevar a cabo el registro de la información del nuevo %s. Por favor intente de nuevo y si el problema persiste reporte la novedad...",NAMEclassSingular);
            var technicalMessage = String.format("Se ha presentado un problema al tratar de registrar la información del nuevo %s en la base de datos postgreSQL. Por favor valide el log de errores para encontrar mayores detalles del problema presentado...",NAMEclassSingular);

            throw DataVictusResidenciasException.crear(userMessage, technicalMessage, exception);
        }
    }

    @Override
    public void delete(UUID data) {
        final StringBuilder statement = new StringBuilder();
        statement.append(DELETE);

        try (final var preparedStatement = getConnection().prepareStatement(statement.toString())) {
            preparedStatement.setObject(1, data);
            preparedStatement.executeUpdate();

        } catch (final SQLException exception) {
            var userMessage = String.format("Se ha presentado un problema tratando de eliminar el %s seleccionado. Por favor intente de nuevo y si el problema persiste reporte la novedad...",NAMEclassSingular);
            var technicalMessage = String.format("Se ha presentado un problema al tratar de eliminar el %s en la base de datos PostgreSQL. Por favor valide el log de errores para encontrar mayores detalles del problema presentado...",NAMEclassSingular);
            throw DataVictusResidenciasException.crear(userMessage, technicalMessage, exception);
        }
    }

    @Override
    public void update(PorteroEntity data) {
        final StringBuilder statement = new StringBuilder();
        statement.append(UPDATE);

        try (final var preparedStatement = getConnection().prepareStatement(statement.toString())) {

            preparedStatement.setString(1, data.getNombre());
            preparedStatement.setString(2,data.getApellido());
            preparedStatement.setString(3,data.getTipo_documento());
            preparedStatement.setString(4,data.getNumero_documento());
            preparedStatement.setString(5,data.getNumero_contacto());
            preparedStatement.setString(6,data.getEmail());
            preparedStatement.setString(7,data.getContraseña());
            preparedStatement.setObject(8, data.getId());

            preparedStatement.executeUpdate();

        } catch (final SQLException exception) {
            var userMessage = String.format("Se ha presentado un problema tratando de actualizar la información del %s. Por favor intente de nuevo y si el problema persiste reporte la novedad...",NAMEclassSingular);
            var technicalMessage = String.format("Se ha presentado un problema al tratar de actualizar la información del %s en la base de datos PostgreSQL. Por favor valide el log de errores para encontrar mayores detalles del problema presentado...",NAMEclassSingular);

            throw DataVictusResidenciasException.crear(userMessage, technicalMessage, exception);
        }
    }
}
