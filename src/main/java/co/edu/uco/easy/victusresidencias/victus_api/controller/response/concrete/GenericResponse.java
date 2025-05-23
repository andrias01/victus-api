package co.edu.uco.easy.victusresidencias.victus_api.controller.response.concrete;


import co.edu.uco.easy.victusresidencias.victus_api.controller.response.Response;

import java.util.List;

public final class GenericResponse extends Response {

	public static final Response build(final List<String> messages) {
		var response= new GenericResponse()	;
		response.setMessages(messages);
		return response;
	}
}
