package co.edu.uco.easy.victusresidencias.victus_api.dao;

import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.exceptions.DataVictusResidenciasException;
import co.edu.uco.easy.victusresidencias.victus_api.dao.enums.DAOSource;
import co.edu.uco.easy.victusresidencias.victus_api.dao.impl.postgresql.PostgreSqlDAOFactory;

public abstract class DAOFactory {
	
	public final static DAOFactory getFactory(final DAOSource source) {
			switch (source) {
			case POSTGRESQL:
				return new PostgreSqlDAOFactory(); // Retorna una instancia de PostgreSqlDAOFactory
			case SQLSERVER:
				return null;
			default:
				var userMessage = "Se ha presentado un problema tratando de llevar a cabo la "
						+ "transacción del factory. "
						+ "Por favor intente de nuevo y si el problema persiste reporte la novedad ...";
				var technicalMessege = "Se ha presentado un problema al tratar de hacer una transacción "
						+ "sobre el objeto deseado "
						+ "revisar el log de errores para mayores detalles del problema presentado...";
				
				throw DataVictusResidenciasException.crear(userMessage, technicalMessege);
			}
	}
	protected abstract void openConnection();

	public abstract void initTransaction();
	
	public abstract void commitTransaction();
	
	public abstract void rollbackTransaction();
	
	public abstract void closeConnection();
	
	public abstract CityDAO getCityDAO();
	public abstract CountryDAO getCountryDAO();
	public abstract StateDAO getStateDAO();
	public abstract PorteroDAO getPorteroDAO();
	public abstract ResidentialComplexDAO getResidentialComplexDAO();
	public abstract CommonZoneDAO getCommonZoneDAO();
	public abstract AdministratorDAO getAdministratorDAO();
}
