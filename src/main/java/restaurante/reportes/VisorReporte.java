package restaurante.reportes;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.swing.JRViewer;
import restaurante.ui.EnviarCorreoDialog;
import restaurante.ui.util.Dialogos;

/**
 * Ventana que muestra un reporte y permite guardarlo en PDF o enviarlo por
 * correo. Es una ventana normal: se puede mover, redimensionar y maximizar.
 */
public class VisorReporte extends JFrame {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(VisorReporte.class.getName());
    private static final DateTimeFormatter SELLO = DateTimeFormatter.ofPattern("yyyyMMdd-HHmm");

    private final transient JasperPrint impresion;
    private final String nombreArchivo;

    public VisorReporte(String titulo, String nombreReporte, JasperPrint impresion) {
        super("Reporte - " + titulo);
        this.impresion = impresion;
        this.nombreArchivo = nombreReporte + "-" + LocalDateTime.now().format(SELLO) + ".pdf";
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().add(new JRViewer(impresion), BorderLayout.CENTER);

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnPdf = new JButton("Guardar PDF...");
        btnPdf.setMnemonic(KeyEvent.VK_G);
        btnPdf.addActionListener(e -> guardarPdf());
        JButton btnCorreo = new JButton("Enviar por correo...");
        btnCorreo.setMnemonic(KeyEvent.VK_E);
        btnCorreo.addActionListener(e -> enviarPorCorreo(titulo));
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());
        acciones.add(btnPdf);
        acciones.add(btnCorreo);
        acciones.add(btnCerrar);
        getContentPane().add(acciones, BorderLayout.SOUTH);
        getRootPane().registerKeyboardAction(e -> dispose(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);

        Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(Math.min(1000, pantalla.width * 9 / 10), Math.min(780, pantalla.height * 9 / 10));
        setLocationRelativeTo(null);
    }

    private void guardarPdf() {
        JFileChooser selector = new JFileChooser(carpetaDescargas());
        selector.setDialogTitle("Guardar reporte como PDF");
        selector.setFileFilter(new FileNameExtensionFilter("Documentos PDF (*.pdf)", "pdf"));
        selector.setSelectedFile(new File(carpetaDescargas(), nombreArchivo));
        if (selector.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File destino = selector.getSelectedFile();
        if (!destino.getName().toLowerCase().endsWith(".pdf")) {
            destino = new File(destino.getParentFile(), destino.getName() + ".pdf");
        }
        if (destino.exists() && !Dialogos.confirmar(this, "El archivo " + destino.getName()
                + " ya existe. ¿Desea reemplazarlo?")) {
            return;
        }
        try {
            JasperExportManager.exportReportToPdfFile(impresion, destino.getAbsolutePath());
            Dialogos.info(this, "Reporte guardado en:\n" + destino.getAbsolutePath());
        } catch (JRException e) {
            LOG.log(Level.SEVERE, "No se pudo exportar el PDF", e);
            Dialogos.error(this, "No se pudo guardar el PDF: " + e.getMessage());
        }
    }

    private void enviarPorCorreo(String titulo) {
        try {
            File temporal = new File(Files.createTempDirectory("reporte").toFile(), nombreArchivo);
            temporal.deleteOnExit();
            temporal.getParentFile().deleteOnExit();
            JasperExportManager.exportReportToPdfFile(impresion, temporal.getAbsolutePath());
            EnviarCorreoDialog.abrir(this, "Reporte: " + titulo, List.of(temporal));
        } catch (IOException | JRException e) {
            LOG.log(Level.SEVERE, "No se pudo preparar el PDF para el correo", e);
            Dialogos.error(this, "No se pudo preparar el PDF: " + e.getMessage());
        }
    }

    private static File carpetaDescargas() {
        File descargas = new File(System.getProperty("user.home"), "Downloads");
        return descargas.isDirectory() ? descargas : new File(System.getProperty("user.home"));
    }
}
