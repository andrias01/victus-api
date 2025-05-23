package co.edu.uco.easy.victusresidencias.victus_api.crosscutting.helpers;

public final class NumericHelper {

	private static final NumericHelper instance;
	public static final int CERO = 0; //nueva linea para poner un numero por defecto

	static {
		instance = new NumericHelper();
	}

	private NumericHelper() {

	}

	public static final NumericHelper getNumericHelper() {
		return instance;
	}

	public final <T extends Number> boolean isGreat(final T numberOne, final T numberTwo) {
		return numberOne.doubleValue() > numberTwo.doubleValue();
	}

	public final <T extends Number> boolean isLess(final T numberOne, final T numberTwo) {
		return numberOne.doubleValue() < numberTwo.doubleValue();
	}

	public final <T extends Number> boolean isDifferent(final T numberOne, final T numberTwo) {
		return numberOne.doubleValue() != numberTwo.doubleValue();
	}

	public final <T extends Number> boolean isEqual(final T numberOne, final T numberTwo) {
		return numberOne.doubleValue() == numberTwo.doubleValue();
	}

	public final static <T extends Number> boolean isGreatOrEqual(final T numberOne, final T numberTwo) {
		return numberOne.doubleValue() >= numberTwo.doubleValue();
	}

	public final static <T extends Number> boolean isLessOrEqual(final T numberOne, final T numberTwo) {
		return numberOne.doubleValue() <= numberTwo.doubleValue();
	}

	public final <T extends Number> boolean isBetween(final T number, final T initialLimit, final T finalLimit,
			final boolean includeInitialLimit, final boolean includeFinalLimit) {
		return (includeInitialLimit ? isGreatOrEqual(number, initialLimit) : isGreat(number, initialLimit))
				&& (includeFinalLimit ? isLessOrEqual(number, finalLimit) : isLess(number, finalLimit));
	}
	
	public static <O> O getDefault(final O object, final O defaultObject) {
		return ObjectHelper.isNull(object) ? defaultObject : object;
		// si es True=NUll da defaultObject y si es False=objeto da el objeto
	}
}
