package restaurante.seguridad;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Hash y verificación de contraseñas con PBKDF2-HMAC-SHA256.
 *
 * <p>Formato almacenado: {@code pbkdf2$<iteraciones>$<sal Base64>$<hash Base64>}.
 * Cada contraseña tiene una sal aleatoria propia, por lo que dos usuarios con
 * la misma contraseña tienen hashes distintos.</p>
 */
public final class Contrasenas {

    static final String PREFIJO = "pbkdf2";
    static final int ITERACIONES = 310_000;
    private static final String ALGORITMO = "PBKDF2WithHmacSHA256";
    private static final int BYTES_SAL = 16;
    private static final int BITS_HASH = 256;
    private static final SecureRandom ALEATORIO = new SecureRandom();

    /** Longitud mínima exigida al crear o cambiar una contraseña. */
    public static final int LONGITUD_MINIMA = 8;

    private Contrasenas() {
    }

    /** Genera el hash de una contraseña para guardarlo en la base de datos. */
    public static String hash(char[] contrasena) {
        byte[] sal = new byte[BYTES_SAL];
        ALEATORIO.nextBytes(sal);
        byte[] hash = pbkdf2(contrasena, sal, ITERACIONES);
        Base64.Encoder b64 = Base64.getEncoder();
        return PREFIJO + "$" + ITERACIONES + "$" + b64.encodeToString(sal) + "$" + b64.encodeToString(hash);
    }

    /**
     * Comprueba una contraseña contra el valor almacenado.
     *
     * <p>Por compatibilidad con bases de datos creadas por versiones anteriores,
     * acepta valores guardados en texto plano; en ese caso
     * {@link #requiereActualizacion(String)} devuelve {@code true} para que se
     * reemplacen por un hash tras el inicio de sesión.</p>
     */
    public static boolean verificar(char[] contrasena, String almacenado) {
        if (almacenado == null || contrasena == null) {
            return false;
        }
        if (!esHash(almacenado)) {
            byte[] esperado = almacenado.getBytes(StandardCharsets.UTF_8);
            byte[] recibido = new String(contrasena).getBytes(StandardCharsets.UTF_8);
            return MessageDigest.isEqual(esperado, recibido);
        }
        String[] partes = almacenado.split("\\$");
        if (partes.length != 4) {
            return false;
        }
        try {
            int iteraciones = Integer.parseInt(partes[1]);
            byte[] sal = Base64.getDecoder().decode(partes[2]);
            byte[] esperado = Base64.getDecoder().decode(partes[3]);
            byte[] calculado = pbkdf2(contrasena, sal, iteraciones);
            return MessageDigest.isEqual(esperado, calculado);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /** Indica si el valor almacenado debe sustituirse por un hash nuevo. */
    public static boolean requiereActualizacion(String almacenado) {
        if (!esHash(almacenado)) {
            return true;
        }
        String[] partes = almacenado.split("\\$");
        try {
            return partes.length != 4 || Integer.parseInt(partes[1]) < ITERACIONES;
        } catch (NumberFormatException e) {
            return true;
        }
    }

    /** Comprueba los requisitos mínimos de una contraseña nueva. Devuelve null si es válida. */
    public static String validarNueva(char[] contrasena) {
        if (contrasena == null || contrasena.length < LONGITUD_MINIMA) {
            return "La contraseña debe tener al menos " + LONGITUD_MINIMA + " caracteres.";
        }
        boolean letra = false;
        boolean digito = false;
        for (char ch : contrasena) {
            letra |= Character.isLetter(ch);
            digito |= Character.isDigit(ch);
        }
        if (!letra || !digito) {
            return "La contraseña debe combinar letras y números.";
        }
        return null;
    }

    static boolean esHash(String almacenado) {
        return almacenado != null && almacenado.startsWith(PREFIJO + "$");
    }

    private static byte[] pbkdf2(char[] contrasena, byte[] sal, int iteraciones) {
        PBEKeySpec spec = new PBEKeySpec(contrasena, sal, iteraciones, BITS_HASH);
        try {
            return SecretKeyFactory.getInstance(ALGORITMO).generateSecret(spec).getEncoded();
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("PBKDF2 no está disponible en esta JVM", e);
        } finally {
            spec.clearPassword();
        }
    }
}
