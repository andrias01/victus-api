package co.edu.uco.easy.victusresidencias.victus_api.dao.impl.postgresql;



import java.sql.Connection;





import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.helpers.SqlConnectionHelper;
import co.edu.uco.easy.victusresidencias.victus_api.dao.*;

public final class PostgreSqlDAOFactory extends DAOFactory {

	private Connection connection;
//	private final String url = "jdbc:postgresql://dpg-cvmbeb63jp1c7381j380-a.oregon-postgres.render.com/backendvictusdb";
//	private final String user = "teamvictus";
//	private final String password = "RLR8kFTHUyIwbKO5HymDtLboRe7NnROW";

	private final String url = "jdbc:postgresql://localhost:5432/LocalBaseDatosJava";
	private final String user = "postgres";
	private final String password = "123456";
	//postgresql://postgres:zamaSRiLZrfwYFewRqWwFsgffHrvxSrA@switchyard.proxy.rlwy.net:35862/railway
//	private final String url = "jdbc:postgresql://switchyard.proxy.rlwy.net:35862/railway";
//	private final String user = "postgres";
//	private final String password = "zamaSRiLZrfwYFewRqWwFsgffHrvxSrA";
//
//	private final String urlGeneral = "jdbc:postgresql://postgres:zamaSRiLZrfwYFewRqWwFsgffHrvxSrA@switchyard.proxy.rlwy.net:35862/railway";

	public PostgreSqlDAOFactory() {
		openConnection();
	}

	@Override
	protected void openConnection() {
		SqlConnectionHelper.validateIfConnectionIsOpen(connection);
		//connection =SqlConnectionHelper.openConnectionPostgreSQL(urlGeneral);
		connection = SqlConnectionHelper.openConnectionPostgreSQL(url, user, password);
		System.out.println("Buen trabajo se conecto a la base de datos");
	}

	@Override
	public void initTransaction() {
		SqlConnectionHelper.initTransaction(connection);
	}

	@Override
	public void commitTransaction() {
		SqlConnectionHelper.commitTransaction(connection);
	}

	@Override
	public void rollbackTransaction() {
		SqlConnectionHelper.rollbackTransaction(connection);
	}

	@Override
	
	public void closeConnection() {
		SqlConnectionHelper.closeConnection(connection);
		System.out.println("Buen trabajo se Desconecto a la base de datos");
	}

	@Override
	public CityDAO getCityDAO() {
		return new CityPostgreSqlDAO(connection);
	}

	@Override
	public StateDAO getStateDAO() {
		return new StatePostgreSqlDAO(connection);
				
	}

	@Override
	public PorteroDAO getPorteroDAO() {
		return new PorteroPostgreSqlDAO(connection);
	}

	@Override
	public CountryDAO getCountryDAO() {
		return new CountryPostgreSQLDAO(connection);
	}

	@Override
	public ResidentialComplexDAO getResidentialComplexDAO() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommonZoneDAO getCommonZoneDAO() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AdministratorDAO getAdministratorDAO() {
		return new AdministratorPostgreSQLDAO(connection);
	}



}
