package co.edu.uco.easy.victusresidencias.victus_api.dao;

import co.edu.uco.easy.victusresidencias.victus_api.entity.CommonZoneEntity;

import java.util.UUID;

public interface CommonZoneDAO 
    extends RetrieveDAO<CommonZoneEntity, UUID>,
            CreateDAO<CommonZoneEntity>,
            DeleteDAO<UUID>,
            UpdateDAO<CommonZoneEntity> {
}
