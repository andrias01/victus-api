package co.edu.uco.easy.victusresidencias.victus_api.dao.impl.postgresql;

import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.exceptions.DataVictusResidenciasException;
import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.helpers.TextHelper;
import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.helpers.UUIDHelper;
import co.edu.uco.easy.victusresidencias.victus_api.dao.CountryDAO;
import co.edu.uco.easy.victusresidencias.victus_api.dao.impl.sql.SqlDAO;
import co.edu.uco.easy.victusresidencias.victus_api.entity.CountryEntity;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

final class CountryPostgreSQLDAO extends SqlDAO implements CountryDAO {
	private static final String FROM = "FROM pais ";
	private static final String SELECT = "SELECT id, nombre ";
	private static final String DELETE = "DELETE FROM pais WHERE id = ?";
	private static final String UPDATE = "UPDATE pais SET nombre = ? WHERE id = ?";
	private static final String NAMEclassSingular = "Pais";
	private static final String NAMEclassPlural = "Paises";
	private static final String CREATEstatemente = "INSERT INTO pais(id, nombre) VALUES (?, ?)";

	public CountryPostgreSQLDAO(Connection connection) {
		super(connection);
	}	
	
	@Override
	public CountryEntity fingByID(UUID id) {
		var countryEntityFilter = new CountryEntity();
	    countryEntityFilter.setId(id);
	    var result = findByFilter(countryEntityFilter);
	    return (result.isEmpty()) ? null : result.get(0); // Retorna null si no encuentra
	}
	

	@Override
	public List<CountryEntity> findAll() {return findByFilter(new CountryEntity());}
	

	@Override
	public List<CountryEntity> findByFilter(CountryEntity filter) { //filter datos 
		final var statement = new StringBuilder(); //sentencia SQL
	    final var parameters = new ArrayList<>();  // ?
	    final var resultSelect = new ArrayList<CountryEntity>(); //select para una lista entity
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
	            var countryEntityTmp = new CountryEntity();
	            countryEntityTmp.setId(UUID.fromString(result.getString("id")));
	            System.out.println("id que inserta a la lista " + UUID.fromString(result.getString("id")));
	            countryEntityTmp.setName(result.getString("nombre"));

	            resultSelect.add(countryEntityTmp);
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
            final CountryEntity filter, 
            final List<Object> parameters) {
			if (!UUIDHelper.isDefault(filter.getId())) {
				System.out.println("Sentencia preparada con where " + filter.getId());
				statement.append("WHERE id = ? ");
				parameters.add(filter.getId());
			} else if (!TextHelper.isEmpty(filter.getName())) { // Condición para filtro de nombre
				statement.append("WHERE nombre = ? ");
				parameters.add(filter.getName());
			}//este if es para filtar por id o por nombre
	}
	
	private void createOrderBy(final StringBuilder statement) {statement.append("ORDER BY nombre ASC");}

	@Override
	public void create(CountryEntity data) {
		
	    CountryEntity filter = new CountryEntity();
	    filter.setName(data.getName());
	    if (!findByFilter(filter).isEmpty()) {
			throw DataVictusResidenciasException.crear(
					String.format("El %s ya existe",NAMEclassSingular),
					String.format("No se puede crear un %s con el nombre duplicado: ",NAMEclassSingular) + data.getName());
	    }
	    
	    final StringBuilder statement = new StringBuilder();
		statement.append(CREATEstatemente);

	    // Verificar si el ID es el UUID predeterminado, y si es así, generar uno nuevo.
	    if (UUIDHelper.isDefault(data.getId())) {
	        data.setId(UUIDHelper.generate()); // Genera un UUID único si es el valor predeterminado.
	    }

	    try (final var preparedStatement = getConnection().prepareStatement(statement.toString())) {
	        preparedStatement.setObject(1, data.getId());
	        preparedStatement.setString(2, data.getName());

	        preparedStatement.executeUpdate();
	        System.out.println("Se creó el país con el nombre " + data.getName() + " exitosamente");

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
	public void update(CountryEntity data) {
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
