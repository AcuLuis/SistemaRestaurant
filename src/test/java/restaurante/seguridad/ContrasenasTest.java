package restaurante.seguridad;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ContrasenasTest {

    @Test
    void elHashSeVerificaConLaContrasenaCorrecta() {
        String hash = Contrasenas.hash("Secreta2024".toCharArray());
        assertTrue(hash.startsWith("pbkdf2$" + Contrasenas.ITERACIONES + "$"));
        assertTrue(Contrasenas.verificar("Secreta2024".toCharArray(), hash));
        assertFalse(Contrasenas.verificar("secreta2024".toCharArray(), hash));
        assertFalse(Contrasenas.requiereActualizacion(hash));
    }

    @Test
    void cadaHashUsaUnaSalDistinta() {
        assertNotEquals(Contrasenas.hash("igual123".toCharArray()), Contrasenas.hash("igual123".toCharArray()));
    }

    @Test
    void aceptaContrasenasAntiguasEnTextoPlanoYPideActualizarlas() {
        assertTrue(Contrasenas.verificar("123456789".toCharArray(), "123456789"));
        assertFalse(Contrasenas.verificar("12345678".toCharArray(), "123456789"));
        assertTrue(Contrasenas.requiereActualizacion("123456789"));
    }

    @Test
    void rechazaValoresMalFormadosONulos() {
        assertFalse(Contrasenas.verificar("x".toCharArray(), "pbkdf2$abc$###$###"));
        assertFalse(Contrasenas.verificar("x".toCharArray(), "pbkdf2$1000$solo-tres"));
        assertFalse(Contrasenas.verificar("x".toCharArray(), null));
        assertFalse(Contrasenas.verificar(null, "x"));
    }

    @Test
    void pideActualizarHashesConMenosIteraciones() {
        assertTrue(Contrasenas.requiereActualizacion("pbkdf2$1000$c2Fs$aGFzaA=="));
    }

    @Test
    void validaLosRequisitosDeLasContrasenasNuevas() {
        assertNull(Contrasenas.validarNueva("Restaurante1".toCharArray()));
        assertNotNull(Contrasenas.validarNueva("corta1".toCharArray()));
        assertNotNull(Contrasenas.validarNueva("sololetras".toCharArray()));
        assertNotNull(Contrasenas.validarNueva("1234567890".toCharArray()));
        assertNotNull(Contrasenas.validarNueva(null));
    }

    @Test
    void losTiposDeUsuarioDesconocidosNoTienenPermisosAmplios() {
        assertEquals(TipoUsuario.ADMINISTRADOR, TipoUsuario.deCodigo(1));
        assertEquals(TipoUsuario.TRANSACCIONES, TipoUsuario.deCodigo(99));
        assertTrue(TipoUsuario.ADMINISTRADOR.puedeVerReportes());
        assertFalse(TipoUsuario.MANTENIMIENTO.puedeVerReportes());
        assertFalse(TipoUsuario.REPORTES.puedeRegistrarTransacciones());
        assertEquals(TipoUsuario.REPORTES, TipoUsuario.deDescripcion("reportes"));
    }
}
