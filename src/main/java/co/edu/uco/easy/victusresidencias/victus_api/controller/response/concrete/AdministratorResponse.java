package co.edu.uco.easy.victusresidencias.victus_api.controller.response.concrete;

import co.edu.uco.easy.victusresidencias.victus_api.controller.response.Response;
import co.edu.uco.easy.victusresidencias.victus_api.controller.response.ResponseWithData;
import co.edu.uco.easy.victusresidencias.victus_api.entity.AdministratorEntity;

import java.util.List;

public final class AdministratorResponse extends ResponseWithData<AdministratorEntity>{

	public static final Response build(final List<String> messages,final List<AdministratorEntity> data) {
		var response= new AdministratorResponse();
		response.setMessages(messages);
		response.setData(data);
		return response;
	}
}
