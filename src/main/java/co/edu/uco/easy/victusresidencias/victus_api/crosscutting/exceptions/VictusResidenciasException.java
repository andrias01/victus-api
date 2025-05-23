package co.edu.uco.easy.victusresidencias.victus_api.crosscutting.exceptions;


import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.exceptions.enums.Layer;

public class VictusResidenciasException extends UcoApplicationException {

	private static final long serialVersionUID = 1L;

	public VictusResidenciasException(final String userMessage, final String technicalMessage, final Exception rootException,
			final Layer layer) {
		super(userMessage, technicalMessage, rootException, layer);
	}

	public static VictusResidenciasException crear(final String userMessage, final String technicalMessage,
			final Exception rootException, final Layer layer) {
		return new VictusResidenciasException(userMessage, technicalMessage, rootException, layer);
	}

	public static VictusResidenciasException crear(final String userMessage) {
		return new VictusResidenciasException(userMessage, userMessage, new Exception(), Layer.GENERAL);
	}

	public static VictusResidenciasException crear(final String userMessage, final String technicalMessage) {
		return new VictusResidenciasException(userMessage, technicalMessage, new Exception(), Layer.GENERAL);
	}

}
