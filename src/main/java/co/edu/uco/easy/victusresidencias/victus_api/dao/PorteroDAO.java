package co.edu.uco.easy.victusresidencias.victus_api.dao;

import co.edu.uco.easy.victusresidencias.victus_api.entity.CountryEntity;
import co.edu.uco.easy.victusresidencias.victus_api.entity.PorteroEntity;

import java.util.UUID;

public interface PorteroDAO
    extends RetrieveDAO<PorteroEntity, UUID>,
            CreateDAO<PorteroEntity>,
            DeleteDAO<UUID>,
            UpdateDAO<PorteroEntity>{
}
