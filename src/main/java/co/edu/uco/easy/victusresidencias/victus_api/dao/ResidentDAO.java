package co.edu.uco.easy.victusresidencias.victus_api.dao;

import co.edu.uco.easy.victusresidencias.victus_api.entity.ResidentEntity;

import java.util.UUID;

public interface ResidentDAO 
    extends RetrieveDAO<ResidentEntity, UUID>,
            CreateDAO<ResidentEntity>,
            DeleteDAO<UUID>,
            UpdateDAO<ResidentEntity> {
}
