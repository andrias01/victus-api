package co.edu.uco.easy.victusresidencias.victus_api.controller.response.concrete;

import co.edu.uco.easy.victusresidencias.victus_api.controller.response.Response;
import co.edu.uco.easy.victusresidencias.victus_api.controller.response.ResponseWithData;
import co.edu.uco.easy.victusresidencias.victus_api.entity.StateEntity;

import java.util.List;

public final class StateResponse extends ResponseWithData<StateEntity>{

	public static final Response build(final List<String> messages,final List<StateEntity> data) {
		var response= new StateResponse();
		response.setMessages(messages);
		response.setData(data);
		return response;
	}
}
