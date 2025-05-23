package co.edu.uco.easy.victusresidencias.victus_api.crosscutting.exceptions;

import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.exceptions.enums.Layer;

public class DataVictusResidenciasException extends VictusResidenciasException {

	private static final long serialVersionUID = 1L;

	public DataVictusResidenciasException(final String userMessage, final String technicalMessage, final Exception rootException) {
		super(userMessage, technicalMessage, rootException, Layer.DATA);
	}

	public static final DataVictusResidenciasException crear(final String userMessage, final String technicalMessage,
			final Exception rootException) {
		return new DataVictusResidenciasException(userMessage, technicalMessage, rootException);
	}

	public static final DataVictusResidenciasException crear(final String userMessage) {
		return new DataVictusResidenciasException(userMessage, userMessage, new Exception());
	}

	public static final DataVictusResidenciasException crear(final String userMessage, final String technicalMessage) {
		return new DataVictusResidenciasException(userMessage, technicalMessage, new Exception());
	}

}
