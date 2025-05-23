package co.edu.uco.easy.victusresidencias.victus_api.dao;

import co.edu.uco.easy.victusresidencias.victus_api.entity.PropertyEntity;

import java.util.UUID;

public interface PropertyDAO 
    extends RetrieveDAO<PropertyEntity, UUID>,
            CreateDAO<PropertyEntity>,
            DeleteDAO<UUID>,
            UpdateDAO<PropertyEntity> {
}
