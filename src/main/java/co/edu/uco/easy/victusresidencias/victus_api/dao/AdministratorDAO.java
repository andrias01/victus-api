package co.edu.uco.easy.victusresidencias.victus_api.dao;

import java.util.UUID;


import co.edu.uco.easy.victusresidencias.victus_api.entity.AdministratorEntity;

public interface AdministratorDAO 
    extends RetrieveDAO<AdministratorEntity, UUID>,
            CreateDAO<AdministratorEntity>,
            DeleteDAO<UUID>,
            UpdateDAO<AdministratorEntity> {
}
