package restaurante.servicio;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import restaurante.dao.ProductoDao;
import restaurante.dao.VentaDao;
import restaurante.datos.DatosException;
import restaurante.datos.Jdbc;
import restaurante.modelo.CabOrdenDeVenta;
import restaurante.modelo.DetOrdenDeVenta;
import restaurante.modelo.Producto;

/**
 * Reglas de negocio de las ventas: registro con control de existencias y
 * anulación con reposición de existencias. Cada operación es una transacción:
 * o se aplica completa o no se aplica nada.
 */
public class VentaServicio {

    /** Longitud máxima del motivo de anulación. */
    public static final int MAXIMO_MOTIVO = 200;

    private final VentaDao ventaDao;
    private final ProductoDao productoDao;

    public VentaServicio() {
        this(new VentaDao(), new ProductoDao());
    }

    VentaServicio(VentaDao ventaDao, ProductoDao productoDao) {
        this.ventaDao = ventaDao;
        this.productoDao = productoDao;
    }

    /**
     * Registra la venta y descuenta las existencias de cada producto.
     *
     * @param ubicacionPedido mesa, dirección de entrega o "Para llevar"
     * @return número de la venta
     * @throws DatosException si falta algún dato o no hay existencias suficientes
     */
    public int registrar(CabOrdenDeVenta cabecera, String ubicacionPedido, List<DetOrdenDeVenta> detalles) {
        Objects.requireNonNull(cabecera, "cabecera");
        if (detalles == null || detalles.isEmpty()) {
            throw new DatosException("La venta debe incluir al menos un producto.");
        }
        for (DetOrdenDeVenta d : detalles) {
            if (d.getCantidad() <= 0) {
                throw new DatosException("La cantidad de cada producto debe ser mayor que cero.");
            }
            if (d.getPrecioUnit() == null || d.getPrecioUnit().signum() < 0) {
                throw new DatosException("Cada producto debe tener un precio válido.");
            }
        }
        return Jdbc.transaccion(c -> {
            for (DetOrdenDeVenta d : detalles) {
                if (!productoDao.descontarExistencias(c, d.getProductoID(), d.getCantidad())) {
                    Producto p = productoDao.buscar(c, d.getProductoID())
                            .orElseThrow(() -> new DatosException("El producto " + d.getProductoID() + " ya no existe."));
                    throw new DatosException("No hay existencias suficientes de \"" + p.getDescripProducto()
                            + "\": se piden " + d.getCantidad() + " y hay " + p.getCantidad() + ".");
                }
            }
            int numero = ventaDao.insertarCabecera(c, cabecera);
            int pedido = ventaDao.insertarPedido(c, cabecera.getEmpleadoID(), ubicacionPedido);
            for (DetOrdenDeVenta d : detalles) {
                ventaDao.insertarDetalle(c, numero, pedido, d);
            }
            return numero;
        });
    }

    /**
     * Anula una venta registrada: la venta se conserva en el historial con el
     * motivo, el usuario y la fecha, y se reponen las existencias vendidas.
     */
    public void anular(int ordenDeVentaID, String motivo, int usuarioID) {
        String texto = motivo == null ? "" : motivo.trim();
        if (texto.isEmpty()) {
            throw new DatosException("Indique el motivo de la anulación.");
        }
        if (texto.length() > MAXIMO_MOTIVO) {
            throw new DatosException("El motivo admite como máximo " + MAXIMO_MOTIVO + " caracteres.");
        }
        Jdbc.transaccion(c -> {
            CabOrdenDeVenta venta = ventaDao.buscar(c, ordenDeVentaID)
                    .orElseThrow(() -> new DatosException("La venta " + ordenDeVentaID + " no existe."));
            if (venta.getEstado() == VentaDao.ESTADO_ANULADA) {
                throw new DatosException("La venta " + ordenDeVentaID + " ya estaba anulada.");
            }
            for (DetOrdenDeVenta d : ventaDao.listarDetalle(c, ordenDeVentaID)) {
                productoDao.sumarExistencias(c, d.getProductoID(), d.getCantidad());
            }
            ventaDao.marcarAnulada(c, ordenDeVentaID, texto, usuarioID);
            return null;
        });
    }

    /** Total de una lista de líneas (precio x cantidad). */
    public static BigDecimal total(List<DetOrdenDeVenta> detalles) {
        return detalles.stream().map(d -> d.getPrecioUnit().multiply(BigDecimal.valueOf(d.getCantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
