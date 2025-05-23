package co.edu.uco.easy.victusresidencias.victus_api.controller.response;

import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.helpers.ObjectHelper;

import java.util.List;

public abstract class ResponseWithData<T> extends Response {
	
	private List<T> data;

	public final List<T> getData() {
		return data;
	}

	public final void setData(final List<T> data) {
		
		this.data = ObjectHelper.getDefault(data, this.data);
	}
	
	

}
