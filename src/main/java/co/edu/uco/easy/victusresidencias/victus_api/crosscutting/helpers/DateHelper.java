package co.edu.uco.easy.victusresidencias.victus_api.crosscutting.helpers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateHelper {

    // Definimos una fecha por defecto
    public static final LocalDate DEFAULT_DATE = LocalDate.of(1900, 1, 1);
    public static final LocalDateTime DEFAULT_DATE_TIME = LocalDateTime.of(1900, 1, 1, 1, 1);
    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

    // Constructor privado para evitar la creación de instancias
    private DateHelper() { }

    // Método para verificar si una fecha es nula
    public static boolean isNull(final LocalDate date) {
        return ObjectHelper.isNull(date);
    }

    // Método para devolver una fecha por defecto si la fecha proporcionada es nula
    public static LocalDate getDefault(final LocalDate date, final LocalDate defaultValue) {
        return ObjectHelper.getDefault(date, defaultValue);
    }

    // Sobrecarga para devolver la fecha predeterminada definida como `DEFAULT_DATE` si es nula
    public static LocalDate getDefault(final LocalDate date) {
        return ObjectHelper.getDefault(date, DEFAULT_DATE);
    }

    // Método para convertir un `String` a `LocalDate` con un formato por defecto
    public static LocalDate parseDate(final String dateString) {
        return parseDate(dateString, DEFAULT_DATE_FORMAT);
    }

    // Método para convertir un `String` a `LocalDate` usando un formato específico
    public static LocalDate parseDate(final String dateString, final String format) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            return LocalDate.parse(ObjectHelper.getDefault(dateString, ""), formatter);
        } catch (DateTimeParseException e) {
            return DEFAULT_DATE;
        }
    }

    // Método para formatear una fecha en un `String` con un formato por defecto
    public static String formatDate(final LocalDate date) {
        return formatDate(date, DEFAULT_DATE_FORMAT);
    }

    // Método para formatear una fecha en un `String` usando un formato específico
    public static String formatDate(final LocalDate date, final String format) {
        var dateToFormat = getDefault(date);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return dateToFormat.format(formatter);
    }

    // Método para comprobar si dos fechas son iguales o si ambas son nulas
    public static boolean isEqual(final LocalDate date1, final LocalDate date2) {
        return getDefault(date1).isEqual(getDefault(date2));
    }

    // Método para verificar si una fecha está vacía (si es igual a la fecha por defecto)
    public static boolean isEmpty(final LocalDate date) {
        return DEFAULT_DATE.equals(getDefault(date));
    }
}
