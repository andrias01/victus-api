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
import co.edu.uco.easy.victusresidencias.victus_api.entity.*;

final class ResidentialComplexPostgreSqlDAO extends SqlDAO implements ResidentialComplexDAO {
	private static final String FROM = "FROM conjunto_residencial ";
	private static final String SELECT = "SELECT id, nombre, telefono_contacto, direccion, descripcion, correo, ciudad_id, administrador_id, portero_id ";
	private static final String DELETE = "DELETE FROM conjunto_residencial WHERE id = ?";
	private static final String UPDATE = "UPDATE conjunto_residencial SET nombre = ?, telefono_contacto = ?, direccion = ?, descripcion = ?, correo = ?, ciudad_id = ?, administrador_id = ?, portero_id = ?  WHERE id = ?";
	private static final String NAMEclassSingular = "Conjunto Residencial";
	private static final String NAMEclassPlural = "Conjuntos Residenciales";
	private static final String CREATEstatemente = "INSERT INTO conjunto_residencial(id, nombre, telefono_contacto, direccion, descripcion, correo, ciudad_id, administrador_id, portero_id ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
		return (result.isEmpty()) ? null : result.get(0);
	}

	@Override
	public List<ResidentialComplexEntity> findAll() {return findByFilter(new ResidentialComplexEntity());}

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
				var cityEntityTmp = new CityEntity();
				var stateEntityTmp = new StateEntity();
				var countryEntityTmp = new CountryEntity();
				var porteroEntityTmp = new PorteroEntity();
				var administradorEntityTmp = new AdministratorEntity();
				residentialComplexEntityTmp.setId(UUID.fromString(result.getString("id")));
				residentialComplexEntityTmp.setNombre(result.getString("nombre"));
				residentialComplexEntityTmp.setTelefono_contacto(result.getString("telefono_contacto"));
				residentialComplexEntityTmp.setDireccion(result.getString("direccion"));
				residentialComplexEntityTmp.setDescription(result.getString("descripcion"));
				residentialComplexEntityTmp.setCorreo(result.getString("correo"));


				cityEntityTmp.setId(UUID.fromString(result.getString("ciudad_id")));
				porteroEntityTmp.setId(UUID.fromString(result.getString("portero_id")));
				administradorEntityTmp.setId(UUID.fromString(result.getString("administrador_id")));

				var entidadPortero = factoria.getPorteroDAO().fingByID(UUID.fromString(result.getString("portero_id")));
				porteroEntityTmp.setNombre(entidadPortero.getNombre());
				porteroEntityTmp.setApellido(entidadPortero.getApellido());
				porteroEntityTmp.setNumero_contacto(entidadPortero.getNumero_contacto());
				porteroEntityTmp.setEmail(entidadPortero.getEmail());
				porteroEntityTmp.setTipo_documento(entidadPortero.getTipo_documento());
				porteroEntityTmp.setNumero_documento(entidadPortero.getNumero_documento());
				porteroEntityTmp.setContraseña(entidadPortero.getContraseña());

				var entidadAdministrador = factoria.getAdministratorDAO().fingByID(UUID.fromString(result.getString("administrador_id")));
				administradorEntityTmp.setName(entidadAdministrador.getName());
				administradorEntityTmp.setLastName(entidadAdministrador.getLastName());
				administradorEntityTmp.setEmail(entidadAdministrador.getEmail());
				administradorEntityTmp.setIdType(entidadAdministrador.getIdType());
				administradorEntityTmp.setIdNumber(entidadAdministrador.getIdNumber());
				administradorEntityTmp.setPassword(entidadAdministrador.getPassword());
				administradorEntityTmp.setContactNumber(entidadAdministrador.getContactNumber());

				var entidadCiudad = factoria.getCityDAO().fingByID(UUID.fromString(result.getString("ciudad_id")));
				cityEntityTmp.setName(entidadCiudad.getName());

				var entidadDepartamento = factoria.getStateDAO().fingByID(entidadCiudad.getState().getId());
				stateEntityTmp.setId(entidadDepartamento.getId());
				stateEntityTmp.setName(entidadDepartamento.getName());

				var entidadPais = entidadDepartamento.getCountry().getId();
				countryEntityTmp.setId(entidadPais);
				countryEntityTmp.setName(factoria.getCountryDAO().fingByID(entidadPais).getName());

				stateEntityTmp.setCountry(countryEntityTmp);
				cityEntityTmp.setState(stateEntityTmp);
				residentialComplexEntityTmp.setCiudad(cityEntityTmp);
				residentialComplexEntityTmp.setPortero(porteroEntityTmp);
				residentialComplexEntityTmp.setAdministrator(administradorEntityTmp);

				resultSelect.add(residentialComplexEntityTmp);
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
							 final ResidentialComplexEntity filter,
							 final List<Object> parameters) {
		if (!UUIDHelper.isDefault(filter.getId())) {
			statement.append("WHERE id = ? ");
			parameters.add(filter.getId());
		}

		if (!TextHelper.isEmpty(filter.getNombre())) {
			statement.append((parameters.isEmpty()) ? "WHERE " : "AND ");
			statement.append("nombre = ? ");
			parameters.add(filter.getNombre());
		}

		if (!UUIDHelper.isDefault(filter.getCiudad().getId())) {
			statement.append((parameters.isEmpty()) ? "WHERE " : "AND ");
			statement.append("ciudad_id = ? ");
			parameters.add(filter.getCiudad().getId());
		}
		if (!UUIDHelper.isDefault(filter.getPortero().getId())) {
			statement.append((parameters.isEmpty()) ? "WHERE " : "AND ");
			statement.append("portero_id = ? ");
			parameters.add(filter.getPortero().getId());
		}
		if (!UUIDHelper.isDefault(filter.getAdministrator().getId())) {
			statement.append((parameters.isEmpty()) ? "WHERE " : "AND ");
			statement.append("administrador_id = ? ");
			parameters.add(filter.getAdministrator().getId());
		}
	}

	private void createOrderBy(final StringBuilder statement) {statement.append("ORDER BY nombre ASC");}

	@Override
	public void create(ResidentialComplexEntity data) {
		ResidentialComplexEntity filterResidentialComplex = new ResidentialComplexEntity();
		filterResidentialComplex.setNombre(data.getNombre());

		if (!findByFilter(filterResidentialComplex).isEmpty()){
			throw DataVictusResidenciasException.crear(
					String.format("El %s ya existe",NAMEclassSingular),
					String.format("No se puede crear un %s con el nombre duplicado: ",NAMEclassSingular) + data.getNombre());
		}
		final StringBuilder statement = new StringBuilder();
		statement.append(CREATEstatemente);
		if (UUIDHelper.isDefault(data.getId())) {
			data.setId(UUIDHelper.generate()); // Genera un UUID único si es el valor predeterminado.
		}
		try (final var preparedStatement = getConnection().prepareStatement(statement.toString())) {
			preparedStatement.setObject(1, data.getId());
			preparedStatement.setString(2, data.getNombre());
			preparedStatement.setString(3, data.getTelefono_contacto());
			preparedStatement.setString(4, data.getDireccion());
			preparedStatement.setString(5, data.getDescription());
			preparedStatement.setString(6, data.getCorreo());
			preparedStatement.setObject(7, data.getCiudad().getId());
			preparedStatement.setObject(8, data.getAdministrator().getId());
			preparedStatement.setObject(9, data.getPortero().getId());

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
	public void update(ResidentialComplexEntity data) {
		final StringBuilder statement = new StringBuilder();
		statement.append(UPDATE);

		try (final var preparedStatement = getConnection().prepareStatement(statement.toString())) {

			preparedStatement.setString(1, data.getNombre());
			preparedStatement.setString(2, data.getTelefono_contacto());
			preparedStatement.setString(3, data.getDireccion());
			preparedStatement.setString(4, data.getDescription());
			preparedStatement.setString(5, data.getCorreo());
			preparedStatement.setObject(6, data.getCiudad().getId());
			preparedStatement.setObject(7, data.getAdministrator().getId());
			preparedStatement.setObject(8, data.getPortero().getId());
			preparedStatement.setObject(9, data.getId());

			preparedStatement.executeUpdate();

		} catch (final SQLException exception) {
			var userMessage = String.format("Se ha presentado un problema tratando de actualizar la información del %s. Por favor intente de nuevo y si el problema persiste reporte la novedad...",NAMEclassSingular);
			var technicalMessage = String.format("Se ha presentado un problema al tratar de actualizar la información del %s en la base de datos PostgreSQL. Por favor valide el log de errores para encontrar mayores detalles del problema presentado...",NAMEclassSingular);
			throw DataVictusResidenciasException.crear(userMessage, technicalMessage, exception);
		}
	}
}
