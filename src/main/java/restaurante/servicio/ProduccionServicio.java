package restaurante.servicio;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import restaurante.dao.InsumoDao;
import restaurante.dao.ProduccionDao;
import restaurante.dao.ProductoDao;
import restaurante.dao.RecetaDao;
import restaurante.datos.DatosException;
import restaurante.datos.Jdbc;
import restaurante.modelo.CabOrdenDeProduccionSemanal;
import restaurante.modelo.CabReceta;
import restaurante.modelo.DetOrdenDeProduccionSemanal;
import restaurante.modelo.DetReceta;
import restaurante.modelo.Insumo;
import restaurante.seguridad.Sesion;
import restaurante.util.Validaciones;

/**
 * Órdenes de producción. Al completar una orden se suman las unidades
 * producidas a las existencias de cada producto y se descuentan de los
 * insumos las cantidades indicadas en su receta.
 */
public class ProduccionServicio {

    private final ProduccionDao dao = new ProduccionDao();
    private final RecetaDao recetaDao = new RecetaDao();
    private final InsumoDao insumoDao = new InsumoDao();
    private final ProductoDao productoDao = new ProductoDao();

    /**
     * Crea una orden pendiente. Si un producto aparece varias veces, sus
     * cantidades se suman en una sola línea.
     */
    public int crear(String fecha, String area, List<DetOrdenDeProduccionSemanal> lineas) {
        if (Validaciones.fecha(fecha).isEmpty()) {
            throw new DatosException("Indique una fecha válida (" + Validaciones.FORMATO_FECHA + ").");
        }
        if (area == null || area.isBlank() || area.length() > 40) {
            throw new DatosException("Indique el área responsable (máximo 40 caracteres).");
        }
        Map<Integer, Integer> cantidades = new LinkedHashMap<>();
        for (DetOrdenDeProduccionSemanal d : lineas == null ? List.<DetOrdenDeProduccionSemanal>of() : lineas) {
            if (d.getCantidad() <= 0) {
                throw new DatosException("La cantidad de cada producto debe ser mayor que cero.");
            }
            cantidades.merge(d.getProductoID(), d.getCantidad(), Integer::sum);
        }
        if (cantidades.isEmpty()) {
            throw new DatosException("La orden debe incluir al menos un producto.");
        }
        CabOrdenDeProduccionSemanal orden = new CabOrdenDeProduccionSemanal();
        orden.setFechaxsem(fecha.trim());
        orden.setDescripArea(area.trim());
        orden.setEmpleadoID(Sesion.actual().map(Sesion::getEmpleadoID).orElse(0));
        return Jdbc.transaccion(c -> {
            int id = dao.insertarCabecera(c, orden);
            for (Map.Entry<Integer, Integer> e : cantidades.entrySet()) {
                dao.insertarDetalle(c, id, new DetOrdenDeProduccionSemanal(e.getKey(), e.getValue()));
            }
            return id;
        });
    }

    /** Completa la orden: suma existencias de productos y consume insumos según las recetas. */
    public void completar(int ordenID) {
        Jdbc.transaccion(c -> {
            dao.bloquear(c, ordenID);
            CabOrdenDeProduccionSemanal orden = dao.buscar(c, ordenID)
                    .orElseThrow(() -> new DatosException("La orden " + ordenID + " no existe."));
            if (orden.getEstado() != CabOrdenDeProduccionSemanal.PENDIENTE) {
                throw new DatosException("La orden " + ordenID + " ya estaba completada.");
            }
            for (DetOrdenDeProduccionSemanal linea : dao.listarDetalle(c, ordenID)) {
                CabReceta receta = recetaDao.buscarPorProducto(c, linea.getProductoID()).orElse(null);
                if (receta != null) {
                    for (DetReceta ingrediente : recetaDao.listarDetalle(c, receta.getRecetaID())) {
                        BigDecimal necesario = ingrediente.getCantidad().multiply(BigDecimal.valueOf(linea.getCantidad()));
                        if (!insumoDao.descontar(c, ingrediente.getInsumoID(), necesario)) {
                            Insumo insumo = insumoDao.buscar(c, ingrediente.getInsumoID()).orElseThrow();
                            throw new DatosException("No hay suficiente \"" + insumo.getDescripInsumo() + "\" para producir "
                                    + linea.getCantidad() + " x " + linea.getDescripProducto() + ": se necesitan "
                                    + necesario.stripTrailingZeros().toPlainString() + " " + insumo.getUnidadMedida()
                                    + " y hay " + insumo.getCantidad().stripTrailingZeros().toPlainString() + ".");
                        }
                    }
                }
                productoDao.sumarExistencias(c, linea.getProductoID(), linea.getCantidad());
            }
            dao.marcarCompletada(c, ordenID);
            return null;
        });
    }

    /** Elimina una orden que todavía no se ha completado. */
    public void eliminar(int ordenID) {
        Jdbc.transaccion(c -> {
            dao.bloquear(c, ordenID);
            CabOrdenDeProduccionSemanal orden = dao.buscar(c, ordenID)
                    .orElseThrow(() -> new DatosException("La orden " + ordenID + " no existe."));
            if (orden.getEstado() != CabOrdenDeProduccionSemanal.PENDIENTE) {
                throw new DatosException("Una orden completada no se puede eliminar: ya modificó las existencias.");
            }
            dao.eliminar(c, ordenID);
            return null;
        });
    }
}
