package co.edu.uco.easy.victusresidencias.victus_api.dao.impl.postgresql;

import java.sql.Connection;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.helpers.TextHelper;
import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.helpers.UUIDHelper;
import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.exceptions.DataVictusResidenciasException;
import co.edu.uco.easy.victusresidencias.victus_api.dao.StateDAO;
import co.edu.uco.easy.victusresidencias.victus_api.dao.impl.sql.SqlDAO;
import co.edu.uco.easy.victusresidencias.victus_api.entity.CountryEntity;
import co.edu.uco.easy.victusresidencias.victus_api.entity.StateEntity;


final class StatePostgreSqlDAO extends SqlDAO implements StateDAO {
	private static final String FROM = "FROM departamento ";
	private static final String SELECT = "SELECT id, nombre, pais_id ";
	private static final String DELETE = "DELETE FROM departamento WHERE id = ?";
	private static final String UPDATE = "UPDATE departamento SET nombre = ? WHERE id = ?";
	private static final String NAMEclassSingular = "Departamento";
	private static final String NAMEclassPlural = "Departamentos";
	private static final String CREATEstatemente = "INSERT INTO departamento(id, nombre, pais_id) VALUES (?, ?, ?)";
	private static final PostgreSqlDAOFactory factoria = new PostgreSqlDAOFactory();
	
	protected StatePostgreSqlDAO(final Connection connection) {
		super(connection);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public StateEntity fingByID(UUID id) {
		var stateEntityFilter = new StateEntity();
	    stateEntityFilter.setId(id);
	    
	    var result = findByFilter(stateEntityFilter);
	    return (result.isEmpty()) ? null : result.get(0);
	}

	@Override
	public List<StateEntity> findAll() {return findByFilter(new StateEntity());}

	@Override
	public List<StateEntity> findByFilter(StateEntity filter) {
		final var statement = new StringBuilder();
	    final var parameters = new ArrayList<>();
	    final var resultSelect = new ArrayList<StateEntity>();

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
	        
	        statementWasPrepared = true;
	        
	        final var result = preparedStatement.executeQuery();
	        while (result.next()) {
	            var stateEntityTmp = new StateEntity();
	            var countryEntityTmp = new CountryEntity();
	            stateEntityTmp.setId(UUID.fromString(result.getString("id")));
	            stateEntityTmp.setName(result.getString("nombre"));
				//Aqui entra el id del pais
	            countryEntityTmp.setId(UUID.fromString(result.getString("pais_id")));

				//esto es para que muestre el nombre del pais cuando busque el departamento

				var entidadPais = factoria.getCountryDAO().fingByID(UUID.fromString(result.getString("pais_id")));
				//Aqui entra el nombre del pais
				countryEntityTmp.setName(entidadPais.getName());

	            stateEntityTmp.setCountry(countryEntityTmp);
	            
	            resultSelect.add(stateEntityTmp);		
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

	private void createFrom(final StringBuilder statement) {
	    statement.append(FROM);
	}

	private void createWhere(final StringBuilder statement,
							 final StateEntity filter,
							 final List<Object> parameters) {

		if (!UUIDHelper.isDefault(filter.getId())) {
			statement.append("WHERE id = ? ");
			parameters.add(filter.getId());
		}

		if (!TextHelper.isEmpty(filter.getName())) {
			statement.append((parameters.isEmpty()) ? "WHERE " : "AND ");
			statement.append("nombre = ? ");
			parameters.add(filter.getName());
		}

		if (!UUIDHelper.isDefault(filter.getCountry().getId())) {
			statement.append((parameters.isEmpty()) ? "WHERE " : "AND ");
			statement.append("pais_id = ? ");
			parameters.add(filter.getCountry().getId());
		}

	}

	private void createOrderBy(final StringBuilder statement) {
	    statement.append("ORDER BY nombre ASC");
	}

	@Override
	public void create(StateEntity data) {
		StateEntity filterDepartamento = new StateEntity();
		filterDepartamento.setName(data.getName());

		if (!findByFilter(filterDepartamento).isEmpty()){
			throw DataVictusResidenciasException.crear(
					String.format("El %s ya existe",NAMEclassSingular),
					String.format("No se puede crear un %s con el nombre duplicado: ",NAMEclassSingular) + data.getName());
		}
		final StringBuilder statement = new StringBuilder();
		statement.append(CREATEstatemente);
		if (UUIDHelper.isDefault(data.getId())) {
			data.setId(UUIDHelper.generate()); // Genera un UUID único si es el valor predeterminado.
		}
		try(final var preparedStatement = getConnection().prepareStatement(statement.toString())) {
			preparedStatement.setObject(1,data.getId());
			preparedStatement.setString(2,data.getName());
			preparedStatement.setObject(3,data.getCountry().getId());
			preparedStatement.executeUpdate();
		}catch (final SQLException exception){
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
	public void update(StateEntity data) {
		final StringBuilder statement = new StringBuilder();
		statement.append(UPDATE);

		try (final var preparedStatement = getConnection().prepareStatement(statement.toString())) {

			preparedStatement.setString(1, data.getName());
			preparedStatement.setObject(2, data.getId());


			preparedStatement.executeUpdate();

		} catch (final SQLException exception) {
			var userMessage = String.format("Se ha presentado un problema tratando de actualizar la información del %s. Por favor intente de nuevo y si el problema persiste reporte la novedad...",NAMEclassSingular);
			var technicalMessage = String.format("Se ha presentado un problema al tratar de actualizar la información del %s en la base de datos PostgreSQL. Por favor valide el log de errores para encontrar mayores detalles del problema presentado...",NAMEclassSingular);

			throw DataVictusResidenciasException.crear(userMessage, technicalMessage, exception);
		}
	}

}
