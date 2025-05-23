package co.edu.uco.easy.victusresidencias.victus_api.controller.response.concrete;

import co.edu.uco.easy.victusresidencias.victus_api.controller.response.Response;
import co.edu.uco.easy.victusresidencias.victus_api.controller.response.ResponseWithData;
import co.edu.uco.easy.victusresidencias.victus_api.entity.CityEntity;

import java.util.List;

public final class CityResponse extends ResponseWithData<CityEntity>{

	public static final Response build(final List<String> messages,final List<CityEntity> data) {
		var response= new CityResponse();
		response.setMessages(messages);
		response.setData(data);
		return response;
	}
}
