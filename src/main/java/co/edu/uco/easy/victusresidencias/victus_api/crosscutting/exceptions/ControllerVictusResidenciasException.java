package co.edu.uco.easy.victusresidencias.victus_api.crosscutting.exceptions;

import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.exceptions.enums.Layer;

public class ControllerVictusResidenciasException extends VictusResidenciasException {

	private static final long serialVersionUID = 1L;

	public ControllerVictusResidenciasException(final String userMessage, final String technicalMessage,
			final Exception rootException) {
		super(userMessage, technicalMessage, rootException, Layer.CONTROLLER);
	}

	public static final ControllerVictusResidenciasException crear(final String userMessage, final String technicalMessage,
			final Exception rootException) {
		return new ControllerVictusResidenciasException(userMessage, technicalMessage, rootException);
	}

	public static final ControllerVictusResidenciasException crear(final String userMessage) {
		return new ControllerVictusResidenciasException(userMessage, userMessage, new Exception());
	}

	public static final ControllerVictusResidenciasException crear(final String userMessage, final String technicalMessage) {
		return new ControllerVictusResidenciasException(userMessage, technicalMessage, new Exception());
	}

}
