package restaurante.ui.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class ValidacionesTest {

    @Test
    void enteros() {
        assertEquals(Optional.of(15), Validaciones.entero(" 15 "));
        assertTrue(Validaciones.entero("1.5").isEmpty());
        assertTrue(Validaciones.entero("abc").isEmpty());
        assertTrue(Validaciones.entero("").isEmpty());
        assertTrue(Validaciones.enteroPositivo("0").isEmpty());
        assertEquals(Optional.of(0), Validaciones.enteroNoNegativo("0"));
        assertTrue(Validaciones.enteroNoNegativo("-1").isEmpty());
    }

    @Test
    void decimalesAceptanComaYPunto() {
        assertEquals(Optional.of(12.5), Validaciones.decimalNoNegativo("12,5"));
        assertEquals(Optional.of(12.5), Validaciones.decimalNoNegativo("12.5"));
        assertTrue(Validaciones.decimalNoNegativo("-3").isEmpty());
        assertTrue(Validaciones.decimalNoNegativo("NaN").isEmpty());
        assertTrue(Validaciones.decimalNoNegativo("diez").isEmpty());
    }

    @Test
    void fechasEstrictas() {
        assertEquals(Optional.of(LocalDate.of(2024, 2, 29)), Validaciones.fecha("2024-02-29"));
        assertTrue(Validaciones.fecha("2023-02-29").isEmpty());
        assertTrue(Validaciones.fecha("2024-2-1").isEmpty());
        assertTrue(Validaciones.fecha("01/02/2024").isEmpty());
    }

    @Test
    void documentosYContacto() {
        assertTrue(Validaciones.esDni("12345678"));
        assertFalse(Validaciones.esDni("1234567"));
        assertFalse(Validaciones.esDni("1234567A"));
        assertTrue(Validaciones.esCorreo("persona@example.com"));
        assertFalse(Validaciones.esCorreo("persona@"));
        assertFalse(Validaciones.esCorreo("sin-arroba.com"));
        assertTrue(Validaciones.esTelefono("987654321"));
        assertFalse(Validaciones.esTelefono("98765"));
        assertFalse(Validaciones.esTelefono("+51987654321"));
    }

    @Test
    void importesConDosDecimales() {
        assertEquals("28.00", Tablas.importe(28));
        assertEquals("0.10", Tablas.importe(0.1));
    }
}
