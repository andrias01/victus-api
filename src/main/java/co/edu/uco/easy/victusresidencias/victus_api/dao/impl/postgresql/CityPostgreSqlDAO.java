package co.edu.uco.easy.victusresidencias.victus_api.dao.impl.postgresql;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.helpers.TextHelper;
import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.helpers.UUIDHelper;
import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.exceptions.DataVictusResidenciasException;
import co.edu.uco.easy.victusresidencias.victus_api.dao.CityDAO;
import co.edu.uco.easy.victusresidencias.victus_api.dao.impl.sql.SqlDAO;
import co.edu.uco.easy.victusresidencias.victus_api.entity.CityEntity;
import co.edu.uco.easy.victusresidencias.victus_api.entity.CountryEntity;
import co.edu.uco.easy.victusresidencias.victus_api.entity.StateEntity;


final class CityPostgreSqlDAO extends SqlDAO implements CityDAO {
	private static final String FROM = "FROM ciudad ";
	private static final String SELECT = "SELECT id, nombre, departamento_id ";
	private static final String DELETE = "DELETE FROM ciudad WHERE id = ?";
	private static final String UPDATE = "UPDATE ciudad SET nombre = ? WHERE id = ?";
	private static final String NAMEclassSingular = "Ciudad";
	private static final String NAMEclassPlural = "Ciudades";
	private static final String CREATEstatemente = "INSERT INTO ciudad(id, nombre, departamento_id) VALUES (?, ?, ?)";
	private static final PostgreSqlDAOFactory factoria = new PostgreSqlDAOFactory();

	protected CityPostgreSqlDAO(final Connection connection) {
		super(connection);
		// TODO Auto-generated constructor stub
	}
	@Override
	public CityEntity fingByID(UUID id) {
		var cityEntityFilter = new CityEntity();
	    cityEntityFilter.setId(id);
	    var result = findByFilter(cityEntityFilter);
	    return (result.isEmpty()) ? null : result.get(0);
	}

	@Override
	public List<CityEntity> findAll() {
		return findByFilter(new CityEntity());
	}

	@Override
	public List<CityEntity> findByFilter(CityEntity filter) {
		final var statement = new StringBuilder();
	    final var parameters = new ArrayList<>();
	    final var resultSelect = new ArrayList<CityEntity>();
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
	            var cityEntityTmp = new CityEntity();
	            var stateEntityTmp = new StateEntity();
				var countryEntityTmp = new CountryEntity();
	            cityEntityTmp.setId(UUID.fromString(result.getString("id")));
	            cityEntityTmp.setName(result.getString("nombre"));

	            stateEntityTmp.setId(UUID.fromString(result.getString("departamento_id")));

				var entidadDepartamento = factoria.getStateDAO().fingByID(UUID.fromString(result.getString("departamento_id")));
	            stateEntityTmp.setName(entidadDepartamento.getName());

				var entidadPais = entidadDepartamento.getCountry().getId();
				countryEntityTmp.setId(entidadPais);
	            countryEntityTmp.setName(factoria.getCountryDAO().fingByID(entidadPais).getName());

				stateEntityTmp.setCountry(countryEntityTmp);
				cityEntityTmp.setState(stateEntityTmp);
	           
	            resultSelect.add(cityEntityTmp);		
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
							 final CityEntity filter,
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

		if (!UUIDHelper.isDefault(filter.getState().getId())) {
			statement.append((parameters.isEmpty()) ? "WHERE " : "AND ");
			statement.append("departameto_id = ? ");
			parameters.add(filter.getState().getId());
		}
	}

	private void createOrderBy(final StringBuilder statement) {statement.append("ORDER BY nombre ASC");}

	@Override
	public void create(CityEntity data) {
		CityEntity filterCiudad = new CityEntity();
		filterCiudad.setName(data.getName());

		if (!findByFilter(filterCiudad).isEmpty()){
			throw DataVictusResidenciasException.crear(
					String.format("El %s ya existe",NAMEclassSingular),
					String.format("No se puede crear un %s con el nombre duplicado: ",NAMEclassSingular) + data.getName());
		}
		final StringBuilder statement = new StringBuilder();
		statement.append(CREATEstatemente);
		if (UUIDHelper.isDefault(data.getId())) {
			data.setId(UUIDHelper.generate()); // Genera un UUID único si es el valor predeterminado.
		}
		try (final var preparedStatement = getConnection().prepareStatement(statement.toString())) {
			preparedStatement.setObject(1, data.getId());
			preparedStatement.setString(2, data.getName());
			preparedStatement.setObject(3, data.getState().getId());
 
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
	public void update(CityEntity data) {
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
