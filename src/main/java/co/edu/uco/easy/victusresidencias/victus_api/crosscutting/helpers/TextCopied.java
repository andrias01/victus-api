package co.edu.uco.easy.victusresidencias.victus_api.crosscutting.helpers;

public class TextCopied {
	// Método para validar si una cadena representa un número (double)
    public static Double parseNumber(final String number) {
        // Validamos si el string es nulo usando el método de TextHelper
        if (TextHelper.isNull(number)) {
            return null;
        }
        try {
            return Double.parseDouble(number); // Convertir a double
        } catch (NumberFormatException e) {
        	System.out.println(e+ " <-- No es un numero valido");
            return null; // Si no es un número válido, retorna null.
        }
    }

    // Método para validar si un número es mayor que otro
    public static boolean isGreaterThan(Double num1, Double num2) {
        if (num1 == null || num2 == null) {
            return false; // Si uno de los números es nulo, no se puede comparar.
        }
        return num1 > num2;
    }

    // Método para validar si un número es menor que otro
    public static boolean isLessThan(Double num1, Double num2) {
        if (num1 == null || num2 == null) {
            return false;
        }
        return num1 < num2;
    }

    // Método para validar si dos números son iguales
    public static boolean areEqual(Double num1, Double num2) {
        if (num1 == null || num2 == null) {
            return false;
        }
        return Double.compare(num1, num2) == 0; // Comparación segura de doubles
    }

    // Método para validar si dos números son diferentes
    public static boolean areDifferent(Double num1, Double num2) {
        return !areEqual(num1, num2);
    }

    // Método para validar si un número pertenece a un rango
    public static boolean isInRange(Double number, Double lowerBound, boolean isLowerInclusive, 
                                    Double upperBound, boolean isUpperInclusive) {
        if (number == null || lowerBound == null || upperBound == null) {
            return false;
        }

        boolean isGreaterThanLower = isLowerInclusive ? number >= lowerBound : number > lowerBound;
        boolean isLessThanUpper = isUpperInclusive ? number <= upperBound : number < upperBound;

        return isGreaterThanLower && isLessThanUpper;
    }

    // Método para validar si un número es positivo
    public static boolean isPositive(Double number) {
        if (number == null) {
            return false;
        }
        return number > 0;
    }

    // Método para validar si un número es negativo
    public static boolean isNegative(Double number) {
        if (number == null) {
            return false;
        }
        return number < 0;
    }
//    
//    public static void main(String[] args) {
//    	Double num1 = NumericHelper.parseNumber("4.6");
//        Double num2 = NumericHelper.parseNumber("10.0");
//
//        System.out.println("¿Es mayor "+num1+" que " +num2+" ?: " + NumericHelper.isGreaterThan(num1, num2));
//        System.out.println("¿Son iguales "+num1+" y "+num2+" ?: " + NumericHelper.areEqual(num1, num2));
//        System.out.println("¿Está el número " +num1+" en el rango (4.5, 9.5]?: " + NumericHelper.isInRange(num1, 4.5, false, 9.5, true));
//        System.out.println("¿Es positivo " +num1+" ?: " + NumericHelper.isPositive(num1));
//	}

}
