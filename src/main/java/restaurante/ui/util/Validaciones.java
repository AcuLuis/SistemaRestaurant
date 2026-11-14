package restaurante.ui.util;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Validaciones de los datos introducidos en los formularios.
 *
 * <p>Evitan que la aplicación falle con NumberFormatException o que se
 * envíen a la base de datos fechas o valores con formato incorrecto.</p>
 */
public final class Validaciones {

    /** Formato de fecha usado en toda la aplicación (ISO 8601). */
    public static final String FORMATO_FECHA = "aaaa-mm-dd";
    private static final DateTimeFormatter FECHA = DateTimeFormatter.ofPattern("uuuu-MM-dd")
            .withResolverStyle(ResolverStyle.STRICT);
    private static final Pattern DNI = Pattern.compile("\\d{8}");
    private static final Pattern CORREO = Pattern.compile("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)*\\.[A-Za-z]{2,}$");
    private static final Pattern TELEFONO = Pattern.compile("\\+?\\d{6,15}");

    private Validaciones() {
    }

    public static boolean vacio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }

    /** Entero (sin decimales); vacío si el texto no es un número entero válido. */
    public static Optional<Integer> entero(String texto) {
        if (vacio(texto)) {
            return Optional.empty();
        }
        try {
            return Optional.of(Integer.parseInt(texto.trim()));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    /** Entero mayor que cero. */
    public static Optional<Integer> enteroPositivo(String texto) {
        return entero(texto).filter(n -> n > 0);
    }

    /** Entero mayor o igual que cero. */
    public static Optional<Integer> enteroNoNegativo(String texto) {
        return entero(texto).filter(n -> n >= 0);
    }

    /** Importe o cantidad decimal no negativa. Acepta coma o punto como separador decimal. */
    public static Optional<Double> decimalNoNegativo(String texto) {
        if (vacio(texto)) {
            return Optional.empty();
        }
        try {
            double valor = Double.parseDouble(texto.trim().replace(',', '.'));
            return Double.isFinite(valor) && valor >= 0 ? Optional.of(valor) : Optional.empty();
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    /** Importe no negativo con como máximo dos decimales (acepta coma o punto). */
    public static Optional<BigDecimal> importe(String texto) {
        return decimal(texto, 2);
    }

    /** Cantidad no negativa con como máximo tres decimales (por ejemplo, 0.250 kg). */
    public static Optional<BigDecimal> cantidad(String texto) {
        return decimal(texto, 3);
    }

    private static Optional<BigDecimal> decimal(String texto, int decimales) {
        if (vacio(texto)) {
            return Optional.empty();
        }
        try {
            BigDecimal valor = new BigDecimal(texto.trim().replace(',', '.'));
            if (valor.signum() < 0 || valor.stripTrailingZeros().scale() > decimales) {
                return Optional.empty();
            }
            return Optional.of(valor.setScale(decimales));
        } catch (NumberFormatException | ArithmeticException e) {
            return Optional.empty();
        }
    }

    /** Fecha con formato aaaa-mm-dd y existente en el calendario (no admite 2024-02-30). */
    public static Optional<LocalDate> fecha(String texto) {
        if (vacio(texto)) {
            return Optional.empty();
        }
        try {
            return Optional.of(LocalDate.parse(texto.trim(), FECHA));
        } catch (DateTimeParseException e) {
            return Optional.empty();
        }
    }

    public static boolean esDni(String texto) {
        return texto != null && DNI.matcher(texto.trim()).matches();
    }

    public static boolean esCorreo(String texto) {
        return texto != null && CORREO.matcher(texto.trim()).matches();
    }

    public static boolean esTelefono(String texto) {
        return texto != null && TELEFONO.matcher(texto.trim()).matches();
    }
}
