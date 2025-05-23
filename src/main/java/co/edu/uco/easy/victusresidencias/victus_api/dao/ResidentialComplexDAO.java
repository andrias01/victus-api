package co.edu.uco.easy.victusresidencias.victus_api.dao;

import java.util.UUID;

import co.edu.uco.easy.victusresidencias.victus_api.entity.ResidentialComplexEntity;

public interface ResidentialComplexDAO 
    extends RetrieveDAO<ResidentialComplexEntity, UUID>,
            CreateDAO<ResidentialComplexEntity>,
            DeleteDAO<UUID>,
            UpdateDAO<ResidentialComplexEntity> {
}
