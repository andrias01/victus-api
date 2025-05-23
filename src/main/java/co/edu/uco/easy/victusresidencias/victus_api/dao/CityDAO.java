package co.edu.uco.easy.victusresidencias.victus_api.dao;

import java.util.UUID;



import co.edu.uco.easy.victusresidencias.victus_api.entity.CityEntity;

public interface CityDAO 
extends RetrieveDAO<CityEntity, UUID>,
CreateDAO<CityEntity>,
DeleteDAO<UUID>,
UpdateDAO<CityEntity> {

}
