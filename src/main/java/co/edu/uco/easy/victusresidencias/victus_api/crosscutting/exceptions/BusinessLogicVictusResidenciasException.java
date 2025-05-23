package co.edu.uco.easy.victusresidencias.victus_api.crosscutting.exceptions;

import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.exceptions.enums.Layer;

public class BusinessLogicVictusResidenciasException extends VictusResidenciasException {

	private static final long serialVersionUID = 1L;

	public BusinessLogicVictusResidenciasException(final String userMessage, final String technicalMessage,
			final Exception rootException) {
		super(userMessage, technicalMessage, rootException, Layer.BUSINESSLOGIC);
	}

	public static final BusinessLogicVictusResidenciasException create(final String userMessage, final String technicalMessage,
			final Exception rootException) {
		return new BusinessLogicVictusResidenciasException(userMessage, technicalMessage, rootException);
	}

	public static final BusinessLogicVictusResidenciasException create(final String userMessage) {
		return new BusinessLogicVictusResidenciasException(userMessage, userMessage, new Exception());
	}

	public static final BusinessLogicVictusResidenciasException create(final String userMessage, final String technicalMessage) {
		return new BusinessLogicVictusResidenciasException(userMessage, technicalMessage, new Exception());
	}

}
