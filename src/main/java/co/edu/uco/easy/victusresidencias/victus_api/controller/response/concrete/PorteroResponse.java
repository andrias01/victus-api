package co.edu.uco.easy.victusresidencias.victus_api.controller.response.concrete;

import co.edu.uco.easy.victusresidencias.victus_api.controller.response.Response;
import co.edu.uco.easy.victusresidencias.victus_api.controller.response.ResponseWithData;
import co.edu.uco.easy.victusresidencias.victus_api.entity.CountryEntity;
import co.edu.uco.easy.victusresidencias.victus_api.entity.PorteroEntity;

import java.util.List;

public class PorteroResponse extends ResponseWithData<PorteroEntity> {
    public static final Response build(final List<String> messages, final List<PorteroEntity> data) {
        var response= new PorteroResponse();
        response.setMessages(messages);
        response.setData(data);
        return response;
    }
}
