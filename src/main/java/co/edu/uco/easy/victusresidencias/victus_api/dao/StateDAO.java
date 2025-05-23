package co.edu.uco.easy.victusresidencias.victus_api.dao;

import java.util.UUID;


import co.edu.uco.easy.victusresidencias.victus_api.entity.StateEntity;

public interface StateDAO
        extends RetrieveDAO<StateEntity, UUID>,
                CreateDAO<StateEntity>,
                DeleteDAO<UUID>,
                UpdateDAO<StateEntity>{

}
