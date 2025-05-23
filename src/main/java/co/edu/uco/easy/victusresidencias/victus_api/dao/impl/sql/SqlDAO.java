package co.edu.uco.easy.victusresidencias.victus_api.dao.impl.sql;

import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.exceptions.DataVictusResidenciasException;
import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.helpers.SqlConnectionHelper;

import java.sql.Connection;


public class SqlDAO {
	private Connection connection;
	
	protected SqlDAO(final Connection connection) {
		setConnection(connection);
	}

	protected Connection getConnection() {
		return connection;
	}
	
	private void setConnection(final Connection connection) {
		validateIfConnectionIsOpen(connection);		
		this.connection = connection;
	}
	
	private void validateIfConnectionIsOpen(final Connection connection) {
		if(!SqlConnectionHelper.connectionIsOpen(connection)) {
			var userMessage = "Se ha presentado un problema inesperado, tratando de llevar "
					+ "a cabo la operación deseada...";
			var technicalMessage = "No es posible crear un acceso a datos de tipo sql con una "
					+ "conexión nula o cerrada...";
			
			throw DataVictusResidenciasException.crear(userMessage, technicalMessage);
		}
	}
	
	
	
	

}
