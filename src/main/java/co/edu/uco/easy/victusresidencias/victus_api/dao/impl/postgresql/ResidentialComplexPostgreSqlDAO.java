package co.edu.uco.easy.victusresidencias.victus_api.dao.impl.postgresql;

import java.sql.Connection;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.helpers.TextHelper;
import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.helpers.UUIDHelper;
import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.exceptions.DataVictusResidenciasException;
import co.edu.uco.easy.victusresidencias.victus_api.dao.ResidentialComplexDAO;
import co.edu.uco.easy.victusresidencias.victus_api.dao.impl.sql.SqlDAO;
import co.edu.uco.easy.victusresidencias.victus_api.entity.AdministratorEntity;
import co.edu.uco.easy.victusresidencias.victus_api.entity.CityEntity;
import co.edu.uco.easy.victusresidencias.victus_api.entity.ResidentialComplexEntity;

final class ResidentialComplexPostgreSqlDAO extends SqlDAO implements ResidentialComplexDAO {
	private static final String FROM = "FROM ciudad ";
	private static final String SELECT = "SELECT id, nombre, departamento_id ";
	private static final String DELETE = "DELETE FROM ciudad WHERE id = ?";
	private static final String UPDATE = "UPDATE ciudad SET nombre = ? WHERE id = ?";
	private static final String NAMEclassSingular = "Conjunto Residencial";
	private static final String NAMEclassPlural = "Conjuntos Residenciales";
	private static final String CREATEstatemente = "INSERT INTO ciudad(id, nombre, departamento_id) VALUES (?, ?, ?)";
	private static final PostgreSqlDAOFactory factoria = new PostgreSqlDAOFactory();

	protected ResidentialComplexPostgreSqlDAO(final Connection connection) {
		super(connection);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public ResidentialComplexEntity fingByID(UUID id) {
		var residentialComplexEntityFilter = new ResidentialComplexEntity();
	    residentialComplexEntityFilter.setId(id);
	    var result = findByFilter(residentialComplexEntityFilter);
//	    return (result.isEmpty()) ? new ResidentialComplexEntity() : result.get(0);
		return (result.isEmpty()) ? null : result.get(0);
	}

	@Override
	public List<ResidentialComplexEntity> findAll() {
		return findByFilter(new ResidentialComplexEntity());
	}

	@Override
	public List<ResidentialComplexEntity> findByFilter(ResidentialComplexEntity filter) {
		final var statement = new StringBuilder();
	    final var parameters = new ArrayList<>();
	    final var resultSelect = new ArrayList<ResidentialComplexEntity>();
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
	            var residentialComplexEntityTmp = new ResidentialComplexEntity();
	            var administratorEntityTmp = new AdministratorEntity();
				var cityEntityTmp = new CityEntity();
				//voy por aqui creando la ciudad temporal pero me di cuenta que falta el portero voy a ir a crearlo
	            residentialComplexEntityTmp.setId(UUID.fromString(result.getString("id")));
	            residentialComplexEntityTmp.setName(result.getString("name"));
	            
	            administratorEntityTmp.setId(UUID.fromString(result.getString("Administrator")));	          
	            residentialComplexEntityTmp.setAdministrator(administratorEntityTmp);
	            
	            resultSelect.add(residentialComplexEntityTmp);		
	        }
	    } catch (final SQLException exception) {
	        var userMessage = "Se ha presentado un problema tratando de llevar a cabo la consulta de los conjuntos residenciales.";
	        var technicalMessage = statementWasPrepared ? 
	            "Problema ejecutando la consulta de los conjuntos residenciales en la base de datos." : 
	            "Problema preparando la consulta de los conjuntos residenciales en la base de datos.";
	        
	        throw DataVictusResidenciasException.crear(userMessage, technicalMessage, exception);
	    }
	    
	    return resultSelect;
	}
	
	private void createSelect(final StringBuilder statement) {
	    statement.append("SELECT id, name, administrator ");
	}

	private void createFrom(final StringBuilder statement) {
	    statement.append("FROM residentialComplex ");
	}

	private void createWhere(final StringBuilder statement, final ResidentialComplexEntity filter, final List<Object> parameters) {
	    if (!UUIDHelper.isDefault(filter.getId())) {
	        statement.append("WHERE id = ? ");
	        parameters.add(filter.getId());
	    }
	    
	    if (!TextHelper.isEmpty(filter.getName())) {
	        statement.append((parameters.isEmpty()) ? "WHERE " : "AND ");
	        statement.append("name = ? ");
	        parameters.add(filter.getName());
	    }
	}

	private void createOrderBy(final StringBuilder statement) {
	    statement.append("ORDER BY name ASC");
	}

	
	//me toco agregar estos metodos de forma automatica,toca revisarlos.
	//
	//
	//
	
	@Override
	public void create(ResidentialComplexEntity data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(UUID data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(ResidentialComplexEntity data) {
		// TODO Auto-generated method stub
		
	}

}
