package co.edu.uco.easy.victusresidencias.victus_api.controller.response;

import co.edu.uco.easy.victusresidencias.victus_api.controller.response.concrete.GenericResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public final class GenerateResponse<T> {


	public static ResponseEntity<GenericResponse> generateSuccessResponse(final List<String> messages){
		var genericResponse = new GenericResponse();
		genericResponse.setMessages(messages);
		return new ResponseEntity<>(genericResponse,HttpStatus.OK);
	}
	
	public static ResponseEntity<GenericResponse> generateFailedResponse(final List<String> messages){
		var genericResponse = new GenericResponse();
		genericResponse.setMessages(messages);
		return new ResponseEntity<>(genericResponse,HttpStatus.BAD_REQUEST);
	}
	
	public ResponseEntity<T> generateSuccessResponseWithData(final T responseWithData){
		return new ResponseEntity<>(responseWithData,HttpStatus.OK);
	}
	                                                                   
	public ResponseEntity<ResponseWithData<T>> generateFaildResponseWithData(final ResponseWithData<T> responseWithData){
		return new ResponseEntity<>(responseWithData,HttpStatus.BAD_REQUEST);
	}
}
