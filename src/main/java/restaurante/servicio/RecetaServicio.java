package restaurante.servicio;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import restaurante.dao.RecetaDao;
import restaurante.datos.DatosException;
import restaurante.datos.Jdbc;
import restaurante.modelo.CabReceta;
import restaurante.modelo.DetReceta;
import restaurante.seguridad.Sesion;

/**
 * Recetas de los productos. Cada producto tiene como máximo una receta, que
 * indica los insumos necesarios para preparar una unidad.
 */
public class RecetaServicio {

    public static final int MAXIMO_PREPARACION = 300;

    private final RecetaDao dao = new RecetaDao();

    /** Crea o reemplaza la receta del producto. */
    public void guardar(int productoID, String preparacion, List<DetReceta> lineas) {
        validar(preparacion, lineas);
        int empleado = Sesion.actual().map(Sesion::getEmpleadoID).orElse(0);
        Jdbc.transaccion(c -> {
            CabReceta receta = dao.buscarPorProducto(c, productoID).orElseGet(CabReceta::new);
            receta.setProductoID(productoID);
            receta.setEmpleadoID(empleado);
            receta.setPreparacion(preparacion == null || preparacion.isBlank() ? null : preparacion.trim());
            if (receta.getRecetaID() == 0) {
                dao.insertarCabecera(c, receta);
            } else {
                dao.actualizarCabecera(c, receta);
                dao.borrarDetalle(c, receta.getRecetaID());
            }
            for (DetReceta d : lineas) {
                dao.insertarDetalle(c, receta.getRecetaID(), d);
            }
            return null;
        });
    }

    public void eliminar(int productoID) {
        Jdbc.transaccion(c -> {
            CabReceta receta = dao.buscarPorProducto(c, productoID)
                    .orElseThrow(() -> new DatosException("El producto no tiene receta."));
            dao.eliminar(c, receta.getRecetaID());
            return null;
        });
    }

    static void validar(String preparacion, List<DetReceta> lineas) {
        if (lineas == null || lineas.isEmpty()) {
            throw new DatosException("La receta debe incluir al menos un insumo.");
        }
        if (preparacion != null && preparacion.trim().length() > MAXIMO_PREPARACION) {
            throw new DatosException("La preparación admite como máximo " + MAXIMO_PREPARACION + " caracteres.");
        }
        Set<Integer> insumos = new HashSet<>();
        for (DetReceta d : lineas) {
            if (d.getCantidad() == null || d.getCantidad().compareTo(BigDecimal.ZERO) <= 0) {
                throw new DatosException("La cantidad de cada insumo debe ser mayor que cero.");
            }
            if (!insumos.add(d.getInsumoID())) {
                throw new DatosException("Un mismo insumo no puede aparecer dos veces en la receta.");
            }
        }
    }
}
