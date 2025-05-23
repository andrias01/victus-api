package co.edu.uco.easy.victusresidencias.victus_api.crosscutting.exceptions;

import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.exceptions.enums.Layer;

public class EntityVictusResidenciasException extends VictusResidenciasException {

	private static final long serialVersionUID = 1L;

	public EntityVictusResidenciasException(final String userMessage, final String technicalMessage, final Exception rootException) {
		super(userMessage, technicalMessage, rootException, Layer.ENTITY);
	}

	public static final EntityVictusResidenciasException crear(final String userMessage, final String technicalMessage,
			final Exception rootException) {
		return new EntityVictusResidenciasException(userMessage, technicalMessage, rootException);
	}

	public static final EntityVictusResidenciasException crear(final String userMessage) {
		return new EntityVictusResidenciasException(userMessage, userMessage, new Exception());
	}

	public static final EntityVictusResidenciasException crear(final String userMessage, final String technicalMessage) {
		return new EntityVictusResidenciasException(userMessage, technicalMessage, new Exception());
	}

}
