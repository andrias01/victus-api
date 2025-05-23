package co.edu.uco.easy.victusresidencias.victus_api.dao.impl.postgresql;

import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.exceptions.DataVictusResidenciasException;
import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.helpers.TextHelper;
import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.helpers.UUIDHelper;
import co.edu.uco.easy.victusresidencias.victus_api.dao.AdministratorDAO;
import co.edu.uco.easy.victusresidencias.victus_api.dao.impl.sql.SqlDAO;
import co.edu.uco.easy.victusresidencias.victus_api.entity.AdministratorEntity;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

final class AdministratorPostgreSQLDAO extends SqlDAO implements AdministratorDAO {
	
	public AdministratorPostgreSQLDAO(Connection connection) {
		super(connection);
	}	
	
	@Override
	public AdministratorEntity fingByID(UUID id) {
		var administratorEntityFilter = new AdministratorEntity();
	    administratorEntityFilter.setId(id);
	    
	    var result = findByFilter(administratorEntityFilter);
	    return (result.isEmpty()) ? null : result.get(0);
	}
	

	@Override
	public List<AdministratorEntity> findAll() {
		AdministratorEntity pruebaAdmin = new AdministratorEntity();
		System.out.println("El admin nuevo tiene el id " + pruebaAdmin.getId());
		return findByFilter(new AdministratorEntity());
	}
	

	@Override
	public List<AdministratorEntity> findByFilter(AdministratorEntity filter) {
		final var statement = new StringBuilder();
	    final var parameters = new ArrayList<>();
	    final var resultSelect = new ArrayList<AdministratorEntity>();
	    var statementWasPrepared = false;
	    
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
	      //SELECT id, name, last_name, id_type, id_number, contact_number, email, password
	        while (result.next()) {
	            var administratorEntityTmp = new AdministratorEntity();
	            administratorEntityTmp.setId(UUID.fromString(result.getString("id")));
	            System.out.println("ID del admin insertado en LISTA para mostar " + UUID.fromString(result.getString("id")));
	            administratorEntityTmp.setName(result.getString("nombre"));
	            administratorEntityTmp.setLastName(result.getString("apellido"));
	            administratorEntityTmp.setIdType(result.getString("tipo_documento"));
	            administratorEntityTmp.setIdNumber(result.getString("numero_documento"));
	            administratorEntityTmp.setContactNumber(result.getString("numero_contacto"));
	            administratorEntityTmp.setEmail(result.getString("email"));
	            administratorEntityTmp.setPassword(result.getString("contraseña"));
	            
	            resultSelect.add(administratorEntityTmp);		
	        }
	    } catch (final SQLException exception) {
	        var userMessage = "Se ha presentado un problema tratando de llevar a cabo la consulta de los administradores.";
	        var technicalMessage = statementWasPrepared ? 
	            "Problema ejecutando la consulta de administradores en la base de datos." : 
	            "Problema preparando la consulta de administradores en la base de datos.";
	        
	        throw DataVictusResidenciasException.crear(userMessage, technicalMessage, exception);
	    }
	    
	    return resultSelect;
	}
	
	private void createSelect(final StringBuilder statement) {
		statement.append("SELECT id, nombre, apellido, tipo_documento, numero_documento, numero_contacto, email, contraseña ");
	}
	
	private void createFrom(final StringBuilder statement) {
		statement.append("FROM administrador ");
	}

	private void createWhere(final StringBuilder statement, 
            final AdministratorEntity filter, 
            final List<Object> parameters) {
			if (!UUIDHelper.isDefault(filter.getId())) {
				System.out.println("Sentencia preparada con where para el ID " + filter.getId());
				statement.append("WHERE id = ? ");
				parameters.add(filter.getId());
			} else if (!TextHelper.isEmpty(filter.getName())) {
				statement.append("WHERE nombre = ? ");
				parameters.add(filter.getName());
			} else if (!TextHelper.isEmpty(filter.getEmail())) {
				System.out.println("Sentencia preparada con where para el EMAIL " + filter.getEmail());
				statement.append("WHERE email = ? ");
				parameters.add(filter.getEmail());
			}
	}
	
	private void createOrderBy(final StringBuilder statement) {
		statement.append("ORDER BY nombre ASC");
	}

	@Override
	public void create(AdministratorEntity data) {
	    AdministratorEntity filter = new AdministratorEntity();
	    AdministratorEntity filterEmail = new AdministratorEntity();
	    filter.setName(data.getName());
	    filterEmail.setEmail(data.getEmail());
	    if (!findByFilter(filter).isEmpty()) {
	        throw DataVictusResidenciasException.crear(
	            "El nombre del administrador ya existe", "No se puede crear un administrador con el nombre duplicado: " + data.getName());
	    }else if(!findByFilter(filterEmail).isEmpty()) {
	        throw DataVictusResidenciasException.crear(
		            "El email del administrador ya existe", "No se puede crear un administrador con el email duplicado: " + data.getEmail() );
	        		
		}
	    
	    final StringBuilder statement = new StringBuilder();
	    statement.append("INSERT INTO administrador(id, nombre, apellido, tipo_documento, numero_documento, numero_contacto, email, contraseña) VALUES(?, ?, ?, ?, ?, ?, ?, ?)");

	    if (UUIDHelper.isDefault(data.getId())) {
	        data.setId(UUIDHelper.generate());
	    }

	    try (final var preparedStatement = getConnection().prepareStatement(statement.toString())) {
	        preparedStatement.setObject(1, data.getId());
	        preparedStatement.setString(2, data.getName());
	        preparedStatement.setString(3, data.getLastName());
	        preparedStatement.setString(4, data.getIdType());
	        preparedStatement.setString(5, data.getIdNumber());
	        preparedStatement.setString(6, data.getContactNumber());
	        preparedStatement.setString(7, data.getEmail());
	        preparedStatement.setString(8, data.getPassword());

	        preparedStatement.executeUpdate();
	        System.out.println("-Se creó el administrador con el nombre " + data.getName() + " exitosamente-");

	    } catch (final SQLException exception) {
	        var userMessage = "Se ha presentado un problema tratando de llevar a cabo el registro del nuevo administrador.";
	        var technicalMessage = "Problema al tratar de registrar el administrador en la base de datos SQL Server.";
	        throw DataVictusResidenciasException.crear(userMessage, technicalMessage, exception);
	    }
	}

	@Override
	public void delete(UUID data) {
		final StringBuilder statement = new StringBuilder();
	    statement.append("DELETE FROM administrador WHERE id = ?");

	    try (final var preparedStatement = getConnection().prepareStatement(statement.toString())) {
	        preparedStatement.setObject(1, data);
	        preparedStatement.executeUpdate();

	    } catch (final SQLException exception) {
	        var userMessage = "Se ha presentado un problema tratando de eliminar el administrador seleccionado.";
	        var technicalMessage = "Problema al tratar de eliminar el administrador en la base de datos SQL Server.";
	        throw DataVictusResidenciasException.crear(userMessage, technicalMessage, exception);
	    }
	}
//SELECT id, name, last_name, id_type, id_number, contact_number, email, password 
	@Override
	public void update(AdministratorEntity data) {
		final StringBuilder statement = new StringBuilder();
	    statement.append("UPDATE administrador SET nombre = ?, apellido = ?, tipo_documento = ?, numero_documento = ?, numero_contacto = ?, email = ?, contraseña = ? WHERE id = ?");

	    try (final var preparedStatement = getConnection().prepareStatement(statement.toString())) {
	        preparedStatement.setString(1, data.getName());
	        preparedStatement.setString(2, data.getLastName());
	        preparedStatement.setString(3, data.getIdType());
	        preparedStatement.setString(4, data.getIdNumber());
	        preparedStatement.setString(5, data.getContactNumber());
	        preparedStatement.setString(6, data.getEmail());
	        preparedStatement.setString(7, data.getPassword());
	        preparedStatement.setObject(8, data.getId());

	        preparedStatement.executeUpdate();

	    } catch (final SQLException exception) {
	        var userMessage = "Se ha presentado un problema tratando de actualizar la información del administrador.";
	        var technicalMessage = "Problema al tratar de actualizar la información del administrador en la base de datos SQL Server.";
	        throw DataVictusResidenciasException.crear(userMessage, technicalMessage, exception);
	    }
	}
}
