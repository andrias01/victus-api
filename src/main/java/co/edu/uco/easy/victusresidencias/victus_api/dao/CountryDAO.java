package co.edu.uco.easy.victusresidencias.victus_api.dao;

import co.edu.uco.easy.victusresidencias.victus_api.entity.CountryEntity;

import java.util.UUID;

public interface CountryDAO 
	extends RetrieveDAO<CountryEntity, UUID>,
			CreateDAO<CountryEntity>,
			DeleteDAO<UUID>,
			UpdateDAO<CountryEntity>{

}
