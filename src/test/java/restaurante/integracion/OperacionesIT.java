package restaurante.integracion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import restaurante.dao.InsumoDao;
import restaurante.dao.ProductoDao;
import restaurante.dao.ReservaDao;
import restaurante.dao.UsuarioDao;
import restaurante.dao.VentaDao;
import restaurante.datos.DatosException;
import restaurante.modelo.CabOrdenDeVenta;
import restaurante.modelo.DetOrdenDeProduccionSemanal;
import restaurante.modelo.DetOrdenDeVenta;
import restaurante.modelo.Producto;
import restaurante.modelo.Reserva;
import restaurante.modelo.Usuario;
import restaurante.servicio.ProduccionServicio;
import restaurante.servicio.ReservaServicio;
import restaurante.servicio.VentaServicio;

/** Flujos de negocio completos sobre una base con datos de ejemplo. */
class OperacionesIT extends BaseDatosIT {

    @BeforeEach
    void prepararBase() throws Exception {
        instalarConDatosDeEjemplo();
    }

    @Test
    void ventaDescuentaExistenciasYAnularLasRepone() {
        Producto lomo = producto("Lomo");
        VentaServicio servicio = new VentaServicio();
        int numero = servicio.registrar(cabecera(), "Salón - Mesa 1", List.of(detalle(lomo, 2)));
        assertEquals(lomo.getCantidad() - 2, producto("Lomo").getCantidad());

        DatosException e = assertThrows(DatosException.class,
                () -> servicio.registrar(cabecera(), "Mesa 1", List.of(detalle(lomo, 999))));
        assertTrue(e.getMensajeUsuario().contains("Lomo saltado"));
        assertEquals(lomo.getCantidad() - 2, producto("Lomo").getCantidad(), "una venta rechazada no cambia nada");

        servicio.anular(numero, "Error de digitación", 101);
        assertEquals(lomo.getCantidad(), producto("Lomo").getCantidad());
        assertEquals(VentaDao.ESTADO_ANULADA, new VentaDao().listarPorCliente(90).get(0).getEstado());
        assertThrows(DatosException.class, () -> servicio.anular(numero, "otra vez", 101));
    }

    @Test
    void completarProduccionConsumeInsumosSegunLasRecetas() {
        BigDecimal arroz = insumo("Arroz");
        int lomo = producto("Lomo").getCantidad();
        ProduccionServicio servicio = new ProduccionServicio();
        servicio.completar(1);
        assertEquals(lomo + 10, producto("Lomo").getCantidad());
        assertEquals(0, arroz.subtract(new BigDecimal("1.5")).compareTo(insumo("Arroz")));
        assertThrows(DatosException.class, () -> servicio.completar(1));

        int orden = servicio.crear(LocalDate.now().toString(), "Preparación",
                List.of(new DetOrdenDeProduccionSemanal(50, 500)));
        BigDecimal embutidos = insumo("Embutidos");
        assertThrows(DatosException.class, () -> servicio.completar(orden));
        assertEquals(0, embutidos.compareTo(insumo("Embutidos")), "sin insumos suficientes no se consume nada");
    }

    @Test
    void lasReservasNoSeSolapanEnLaMismaMesa() {
        Reserva existente = new ReservaDao().listar(false, "").stream().filter(r -> r.getReservaID() == 1)
                .findFirst().orElseThrow();
        Reserva r = new Reserva();
        r.setClienteID(91);
        r.setFecha(existente.getFecha());
        r.setHora("21:00");
        r.setPersonas(2);
        r.setMesaID(existente.getMesaID());
        ReservaServicio servicio = new ReservaServicio();
        assertThrows(DatosException.class, () -> servicio.guardar(r));
        r.setHora("23:00");
        servicio.guardar(r);
        assertTrue(r.getReservaID() > 0);
    }

    @Test
    void noSePuedeEliminarAlUltimoAdministrador() {
        restaurante.seguridad.Sesion.iniciar(new restaurante.seguridad.Sesion(105, "demo.ventas",
                restaurante.seguridad.TipoUsuario.TRANSACCIONES, 1005, 1));
        UsuarioDao dao = new UsuarioDao();
        for (Usuario u : dao.listar(false, "")) {
            if (u.getUsuarioID() != 101 && u.getTipoUsuario() == 1) {
                dao.eliminar(u);
            }
        }
        Usuario admin = new Usuario();
        admin.setUsuarioID(101);
        assertThrows(DatosException.class, () -> dao.eliminar(admin));
    }

    private static CabOrdenDeVenta cabecera() {
        CabOrdenDeVenta cab = new CabOrdenDeVenta();
        cab.setEmpleadoID(1002);
        cab.setClienteID(90);
        cab.setMesaID(1);
        cab.setMetodoPago(1);
        cab.setTipEntrega("ComerEnLocal");
        return cab;
    }

    private static DetOrdenDeVenta detalle(Producto p, int cantidad) {
        DetOrdenDeVenta d = new DetOrdenDeVenta();
        d.setProductoID(p.getProductoID());
        d.setCantidad(cantidad);
        d.setPrecioUnit(p.getPrecioUnit());
        return d;
    }

    private static Producto producto(String nombre) {
        return new ProductoDao().listar(true, nombre).get(0);
    }

    private static BigDecimal insumo(String nombre) {
        return new InsumoDao().listar(true, nombre).get(0).getCantidad();
    }
}
