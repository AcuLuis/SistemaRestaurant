package restaurante.servicio;

import java.math.BigDecimal;
import java.util.List;
import restaurante.dao.InsumoDao;
import restaurante.dao.ProductoDao;
import restaurante.dao.ReservaDao;
import restaurante.datos.Jdbc;
import restaurante.modelo.Insumo;
import restaurante.modelo.Producto;
import restaurante.modelo.Reserva;

/** Datos del panel de inicio: actividad del día y alertas de existencias. */
public class ResumenServicio {

    /** Existencias a partir de las que se avisa de que un producto se está agotando. */
    public static final int MINIMO_PRODUCTO = 5;
    /** Cantidad a partir de la que se avisa de que un insumo se está agotando. */
    public static final BigDecimal MINIMO_INSUMO = new BigDecimal("2");

    /** Número de ventas y total facturado en el día (sin anuladas). */
    public static final class VentasDelDia {
        private final int ventas;
        private final BigDecimal total;

        VentasDelDia(int ventas, BigDecimal total) {
            this.ventas = ventas;
            this.total = total;
        }

        public int getVentas() {
            return ventas;
        }

        public BigDecimal getTotal() {
            return total;
        }
    }

    /** Producto con las unidades vendidas en el día. */
    public static final class ProductoVendido {
        private final String producto;
        private final int unidades;
        private final BigDecimal importe;

        ProductoVendido(String producto, int unidades, BigDecimal importe) {
            this.producto = producto;
            this.unidades = unidades;
            this.importe = importe;
        }

        public String getProducto() {
            return producto;
        }

        public int getUnidades() {
            return unidades;
        }

        public BigDecimal getImporte() {
            return importe;
        }
    }

    private static final String VENTAS_HOY = " FROM CabOrdenDeVenta v"
            + " INNER JOIN DetOrdenDeVenta d ON d.ordenDeVentaID = v.ordenDeVentaID"
            + " INNER JOIN Producto p ON p.productoID = d.productoID"
            + " WHERE v.estado = 1 AND CAST(v.fechaVenta AS date) = CAST(GETDATE() AS date)";
    private static final String IMPORTE = "SUM(d.cantidad * COALESCE(d.precioUnit, p.precioUnit))";

    public VentasDelDia ventasDelDia() {
        return Jdbc.uno("SELECT COUNT(DISTINCT v.ordenDeVentaID), ISNULL(" + IMPORTE + ", 0)" + VENTAS_HOY,
                rs -> new VentasDelDia(rs.getInt(1), rs.getBigDecimal(2))).orElse(new VentasDelDia(0, BigDecimal.ZERO));
    }

    public List<ProductoVendido> masVendidosDelDia(int limite) {
        return Jdbc.lista("SELECT TOP (?) p.descripProducto, SUM(d.cantidad), " + IMPORTE + VENTAS_HOY
                + " GROUP BY p.descripProducto ORDER BY SUM(d.cantidad) DESC",
                rs -> new ProductoVendido(rs.getString(1), rs.getInt(2), rs.getBigDecimal(3)), limite);
    }

    public List<Producto> productosPorAgotarse() {
        return new ProductoDao().listarConPocasExistencias(MINIMO_PRODUCTO);
    }

    public List<Insumo> insumosPorAgotarse() {
        return new InsumoDao().listarConPocasExistencias(MINIMO_INSUMO);
    }

    public List<Reserva> reservasDeHoy() {
        return new ReservaDao().listarPendientesDeHoy();
    }

    public int ordenesPendientes() {
        return Jdbc.uno("SELECT COUNT(*) FROM CabOrdenDeProduccionSemanal WHERE estado = 0", rs -> rs.getInt(1))
                .orElse(0);
    }
}
