package restaurante.servicio;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import restaurante.datos.DatosException;
import restaurante.modelo.DetOrdenDeVenta;
import restaurante.modelo.DetReceta;
import restaurante.modelo.Empleado;

class ServiciosTest {

    @Test
    void validaLasRecetas() {
        DetReceta arroz = new DetReceta(24, new BigDecimal("0.150"));
        assertDoesNotThrow(() -> RecetaServicio.validar("Saltear", List.of(arroz)));
        assertThrows(DatosException.class, () -> RecetaServicio.validar("", List.of()));
        assertThrows(DatosException.class, () -> RecetaServicio.validar("", List.of(arroz, new DetReceta(24, BigDecimal.ONE))));
        assertThrows(DatosException.class, () -> RecetaServicio.validar("", List.of(new DetReceta(1, BigDecimal.ZERO))));
        assertThrows(DatosException.class, () -> RecetaServicio.validar("x".repeat(301), List.of(arroz)));
    }

    @Test
    void validaLaHoraDeLasReservas() {
        assertEquals(LocalTime.of(20, 30), ReservaServicio.hora("20:30"));
        assertThrows(DatosException.class, () -> ReservaServicio.hora("25:00"));
        assertThrows(DatosException.class, () -> ReservaServicio.hora("8 pm"));
        assertThrows(DatosException.class, () -> ReservaServicio.hora(null));
    }

    @Test
    void validaLosDatosDeLosEmpleados() {
        Empleado e = new Empleado();
        e.setNombres("Ana");
        e.setApellidos("Torres");
        e.setDni("40000001");
        e.setFechaNacimiento("1995-03-14");
        assertDoesNotThrow(() -> EmpleadoServicio.validarDatos(e));
        e.setDni("123");
        assertThrows(DatosException.class, () -> EmpleadoServicio.validarDatos(e));
        e.setDni("40000001");
        e.setFechaNacimiento(LocalDate.now().minusYears(10).toString());
        assertThrows(DatosException.class, () -> EmpleadoServicio.validarDatos(e));
    }

    @Test
    void calculaElTotalDeUnaVentaSinErroresDeRedondeo() {
        DetOrdenDeVenta a = new DetOrdenDeVenta();
        a.setCantidad(3);
        a.setPrecioUnit(new BigDecimal("0.10"));
        DetOrdenDeVenta b = new DetOrdenDeVenta();
        b.setCantidad(1);
        b.setPrecioUnit(new BigDecimal("0.20"));
        assertEquals(0, new BigDecimal("0.50").compareTo(VentaServicio.total(List.of(a, b))));
    }
}
