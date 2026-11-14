package restaurante.reportes;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Window;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import restaurante.datos.ConexionBD;
import restaurante.datos.DatosException;
import restaurante.ui.util.Dialogos;

/**
 * Generación de los reportes de JasperReports incluidos en la aplicación.
 *
 * <p>Las plantillas .jrxml se leen del classpath (funcionan igual desde el
 * IDE y desde el JAR) y se compilan una sola vez por ejecución.</p>
 */
public final class Reportes {

    private static final Map<String, JasperReport> COMPILADOS = new ConcurrentHashMap<>();

    private Reportes() {
    }

    /** Llena el reporte con los datos actuales de la base de datos. */
    public static JasperPrint generar(String nombre, Map<String, Object> parametros) throws JRException {
        JasperReport reporte = compilar(nombre);
        try (Connection c = ConexionBD.abrir()) {
            return JasperFillManager.fillReport(reporte, new HashMap<>(parametros), c);
        } catch (SQLException e) {
            throw DatosException.de(e);
        }
    }

    /**
     * Genera el reporte en segundo plano y lo muestra en el visor. Si no hay
     * datos para los criterios indicados, se informa en lugar de abrir un
     * reporte vacío.
     */
    public static void mostrar(Component origen, String nombre, String titulo, Map<String, Object> parametros) {
        Window ventana = origen == null ? null : SwingUtilities.getWindowAncestor(origen);
        Component cursor = ventana != null ? ventana : origen;
        if (cursor != null) {
            cursor.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        }
        new SwingWorker<JasperPrint, Void>() {
            @Override
            protected JasperPrint doInBackground() throws Exception {
                return generar(nombre, parametros);
            }

            @Override
            protected void done() {
                if (cursor != null) {
                    cursor.setCursor(Cursor.getDefaultCursor());
                }
                try {
                    JasperPrint impresion = get();
                    if (impresion.getPages().isEmpty()) {
                        Dialogos.info(origen, "El reporte \"" + titulo + "\" no tiene datos para los criterios indicados.");
                        return;
                    }
                    new VisorReporte(titulo, nombre, impresion).setVisible(true);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (ExecutionException e) {
                    Dialogos.error(origen, e.getCause() instanceof JRException
                            ? new IllegalStateException("No se pudo generar el reporte: " + e.getCause().getMessage(),
                                    e.getCause())
                            : e.getCause());
                }
            }
        }.execute();
    }

    public static void mostrar(Component origen, String nombre, String titulo) {
        mostrar(origen, nombre, titulo, Map.of());
    }

    static JasperReport compilar(String nombre) throws JRException {
        JasperReport reporte = COMPILADOS.get(nombre);
        if (reporte == null) {
            String ruta = "/reportes/" + nombre + ".jrxml";
            try (InputStream in = Reportes.class.getResourceAsStream(ruta)) {
                if (in == null) {
                    throw new JRException("No se encontró la plantilla " + ruta);
                }
                reporte = JasperCompileManager.compileReport(in);
            } catch (java.io.IOException e) {
                throw new JRException("No se pudo leer la plantilla " + ruta, e);
            }
            COMPILADOS.put(nombre, reporte);
        }
        return reporte;
    }
}
