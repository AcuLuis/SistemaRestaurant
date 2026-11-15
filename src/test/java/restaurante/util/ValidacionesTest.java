package restaurante.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import restaurante.ui.util.Tablas;

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
        assertTrue(Validaciones.esTelefono("+51987654321"));
        assertFalse(Validaciones.esTelefono("98765"));
        assertFalse(Validaciones.esTelefono("987-654-321"));
    }

    @Test
    void importesYCantidadesDecimales() {
        assertEquals(Optional.of(new BigDecimal("12.50")), Validaciones.importe("12,5"));
        assertEquals(Optional.of(new BigDecimal("100.00")), Validaciones.importe("100"));
        assertTrue(Validaciones.importe("1.005").isEmpty());
        assertTrue(Validaciones.importe("-1").isEmpty());
        assertEquals(Optional.of(new BigDecimal("0.250")), Validaciones.cantidad("0.25"));
        assertTrue(Validaciones.cantidad("0.2505").isEmpty());
    }

    @Test
    void formatoDeImportesYCantidades() {
        assertEquals("28.00", Tablas.importe(new BigDecimal("28")));
        assertEquals("0.13", Tablas.importe(new BigDecimal("0.125")));
        assertEquals("1.5", Tablas.cantidad(new BigDecimal("1.500")));
        assertEquals("20", Tablas.cantidad(new BigDecimal("20.000")));
    }
}
