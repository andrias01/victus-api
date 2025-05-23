package co.edu.uco.easy.victusresidencias.victus_api.dao.impl.postgresql;

import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.exceptions.DataVictusResidenciasException;
import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.helpers.TextHelper;
import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.helpers.UUIDHelper;
import co.edu.uco.easy.victusresidencias.victus_api.dao.CommonZoneDAO;
import co.edu.uco.easy.victusresidencias.victus_api.dao.impl.sql.SqlDAO;
import co.edu.uco.easy.victusresidencias.victus_api.entity.CommonZoneEntity;
import co.edu.uco.easy.victusresidencias.victus_api.entity.ResidentialComplexEntity;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

final class CommonZonePostgreSqlDAO extends SqlDAO implements CommonZoneDAO {
	
	protected CommonZonePostgreSqlDAO(final Connection connection) {
		super(connection);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public CommonZoneEntity fingByID(UUID id) {
		var commonZoneEntityFilter = new CommonZoneEntity();
		commonZoneEntityFilter.setId(id);
	    
	    var result = findByFilter(commonZoneEntityFilter);
	    return (result.isEmpty()) ? new CommonZoneEntity() : result.get(0);
	}

	@Override
	public List<CommonZoneEntity> findAll() {
		return findByFilter(new CommonZoneEntity());
	}

	@Override
	public List<CommonZoneEntity> findByFilter(CommonZoneEntity filter) {
		final var statement = new StringBuilder();
	    final var parameters = new ArrayList<>();
	    final var resultSelect = new ArrayList<CommonZoneEntity>();
	    var statementWasPrepared = false;		 
	    
	    // Select
	    createSelect(statement);
	    
	    // From
	    createFrom(statement);
	    
	    // Where
	    createWhere(statement, filter, parameters);
	    
	    // Order By
	    createOrderBy(statement);
	    
	    try (var preparedStatement = getConnection().prepareStatement(statement.toString())) {
	        for (var arrayIndex = 0; arrayIndex < parameters.size(); arrayIndex++) {
	            var statementIndex = arrayIndex + 1;
	            preparedStatement.setObject(statementIndex, parameters.get(arrayIndex));
	        }
	        
	        statementWasPrepared = true;
	        
	        final var result = preparedStatement.executeQuery();
	        while (result.next()) {
	            var commonZoneEntityTmp = new CommonZoneEntity();
	            var residentialComplexEntityTmp = new ResidentialComplexEntity();
	            commonZoneEntityTmp.setId(UUID.fromString(result.getString("id")));
	            commonZoneEntityTmp.setName(result.getString("name"));
	            
	            residentialComplexEntityTmp.setId(UUID.fromString(result.getString("residentialComplex")));	          
	            commonZoneEntityTmp.setResidentialComplex(residentialComplexEntityTmp);
	            
	            resultSelect.add(commonZoneEntityTmp);		
	        }
	    } catch (final SQLException exception) {
	        var userMessage = "Se ha presentado un problema tratando de llevar a cabo la consulta de las zonas comúnes.";
	        var technicalMessage = statementWasPrepared ? 
	            "Problema ejecutando la consulta de las zonas comúnes en la base de datos." : 
	            "Problema preparando la consulta de las zonas comúnes en la base de datos.";
	        
	        throw DataVictusResidenciasException.crear(userMessage, technicalMessage, exception);
	    }
	    
	    return resultSelect;
	}
	
	private void createSelect(final StringBuilder statement) {
	    statement.append("SELECT id, name, residentialComplex ");
	}

	private void createFrom(final StringBuilder statement) {
	    statement.append("FROM commonZone ");
	}

	private void createWhere(final StringBuilder statement, final CommonZoneEntity filter, final List<Object> parameters) {
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

	// Revisar tambien Pues sin estas implementaciones me sale error.
	//
	//
	
	@Override
	public void create(CommonZoneEntity data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(UUID data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(CommonZoneEntity data) {
		// TODO Auto-generated method stub
		
	}


}
