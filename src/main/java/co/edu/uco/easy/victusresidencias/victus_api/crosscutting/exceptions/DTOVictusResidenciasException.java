package co.edu.uco.easy.victusresidencias.victus_api.crosscutting.exceptions;

import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.exceptions.enums.Layer;

public class DTOVictusResidenciasException extends VictusResidenciasException {

	private static final long serialVersionUID = 1L;

	public DTOVictusResidenciasException(final String userMessage, final String technicalMessage, final Exception rootException) {
		super(userMessage, technicalMessage, rootException, Layer.DTO);
	}

	public static final DTOVictusResidenciasException crear(final String userMessage, final String technicalMessage,
			final Exception rootException) {
		return new DTOVictusResidenciasException(userMessage, technicalMessage, rootException);
	}

	public static final DTOVictusResidenciasException crear(final String userMessage) {
		return new DTOVictusResidenciasException(userMessage, userMessage, new Exception());
	}

	public static final DTOVictusResidenciasException crear(final String userMessage, final String technicalMessage) {
		return new DTOVictusResidenciasException(userMessage, technicalMessage, new Exception());
	}

}
