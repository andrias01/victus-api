package co.edu.uco.easy.victusresidencias.victus_api.controller.response;

import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.helpers.ObjectHelper;

import java.util.ArrayList;
import java.util.List;


public abstract class Response {

	private List<String> messages = new ArrayList<>();
	
	public final void setMessages(final List<String> messages) {
		this.messages = ObjectHelper.getDefault(messages, this.messages);
	}

	public List<String> getMessages() {
		return messages;
	}
	
	
}
