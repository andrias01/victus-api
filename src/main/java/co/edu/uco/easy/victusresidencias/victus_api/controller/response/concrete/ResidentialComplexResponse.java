package co.edu.uco.easy.victusresidencias.victus_api.controller.response.concrete;

import co.edu.uco.easy.victusresidencias.victus_api.controller.response.Response;
import co.edu.uco.easy.victusresidencias.victus_api.controller.response.ResponseWithData;
import co.edu.uco.easy.victusresidencias.victus_api.entity.ResidentialComplexEntity;

import java.util.List;

public final class ResidentialComplexResponse extends ResponseWithData<ResidentialComplexEntity>{

	public static final Response build(final List<String> messages,final List<ResidentialComplexEntity> data) {
		var response= new ResidentialComplexResponse();
		response.setMessages(messages);
		response.setData(data);
		return response;
	}
}
