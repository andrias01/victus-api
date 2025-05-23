package co.edu.uco.easy.victusresidencias.victus_api.crosscutting.helpers;

public class ObjectHelper {
	
	private ObjectHelper() {
		
	}
	
	public static <O> boolean isNull(final O object) {
		return object == null;
		// si el objeto que entra en igual a NULL me devuelve TRUE
	}
	
	public static <O> O getDefault(final O object, final O defaultObject) {
		return isNull(object) ? defaultObject : object;
		// si es True=NUll da defaultObject y si es False=objeto da el objeto
	}

}
