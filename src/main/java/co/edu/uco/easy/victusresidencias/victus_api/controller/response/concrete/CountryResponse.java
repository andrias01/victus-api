package co.edu.uco.easy.victusresidencias.victus_api.controller.response.concrete;

import co.edu.uco.easy.victusresidencias.victus_api.controller.response.Response;
import co.edu.uco.easy.victusresidencias.victus_api.controller.response.ResponseWithData;
import co.edu.uco.easy.victusresidencias.victus_api.entity.CountryEntity;

import java.util.List;

public final class CountryResponse extends ResponseWithData<CountryEntity>{

	public static final Response build(final List<String> messages,final List<CountryEntity> data) {
		var response= new CountryResponse();
		response.setMessages(messages);
		response.setData(data);
		return response;   
	}
}
