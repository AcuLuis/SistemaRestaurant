package restaurante.ui.reportes;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import restaurante.reportes.Reportes;
import restaurante.ui.util.Dialogos;
import restaurante.ui.util.Iconos;
import restaurante.ui.util.SelectorFecha;
import restaurante.util.Validaciones;

/** Reportes de ventas por día, por producto o por empleado en un rango de fechas. */
public class FrmReporteVentas extends JInternalFrame {

    private static final long serialVersionUID = 1L;
    private static final String[] TIPOS = {"Ventas por día", "Ventas por producto", "Ventas por empleado"};
    private static final String[] PLANTILLAS = {"repVentasPorDia", "repVentasPorProducto", "repVentasPorEmpleado"};

    private final JTextField txtDesde = new JTextField(10);
    private final JTextField txtHasta = new JTextField(10);
    private final JComboBox<String> cmbTipo = new JComboBox<>(TIPOS);

    public FrmReporteVentas() {
        super("Reportes de ventas", true, true, false, true);
        setFrameIcon(Iconos.icono("reportes"));
        LocalDate hoy = LocalDate.now();
        txtDesde.setText(hoy.withDayOfMonth(1).toString());
        txtHasta.setText(hoy.toString());
        SelectorFecha.instalar(txtDesde);
        SelectorFecha.instalar(txtHasta);

        JPanel campos = new JPanel(new GridBagLayout());
        campos.setBorder(BorderFactory.createEmptyBorder(12, 12, 6, 12));
        agregar(campos, 0, "Reporte:", cmbTipo);
        agregar(campos, 1, "Desde:", txtDesde);
        agregar(campos, 2, "Hasta:", txtHasta);

        JButton btnGenerar = new JButton("Generar reporte");
        JButton btnCerrar = new JButton("Cerrar");
        btnGenerar.addActionListener(e -> generar());
        btnCerrar.addActionListener(e -> dispose());
        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        acciones.add(btnGenerar);
        acciones.add(btnCerrar);

        getContentPane().add(campos, BorderLayout.CENTER);
        getContentPane().add(acciones, BorderLayout.SOUTH);
        getRootPane().setDefaultButton(btnGenerar);
        pack();
    }

    private static void agregar(JPanel panel, int fila, String etiqueta, JComponent campo) {
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 4, 4, 4);
        c.gridy = fila;
        c.anchor = GridBagConstraints.LINE_END;
        panel.add(new JLabel(etiqueta), c);
        c.gridx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        panel.add(campo, c);
    }

    private void generar() {
        Optional<LocalDate> desde = Validaciones.fecha(txtDesde.getText());
        Optional<LocalDate> hasta = Validaciones.fecha(txtHasta.getText());
        if (desde.isEmpty() || hasta.isEmpty()) {
            Dialogos.aviso(this, "Indique fechas válidas (" + Validaciones.FORMATO_FECHA + ").");
            return;
        }
        if (hasta.get().isBefore(desde.get())) {
            Dialogos.aviso(this, "La fecha final no puede ser anterior a la inicial.");
            return;
        }
        int tipo = cmbTipo.getSelectedIndex();
        Reportes.mostrar(this, PLANTILLAS[tipo], TIPOS[tipo] + " del " + desde.get() + " al " + hasta.get(),
                Map.of("fechaIni", desde.get().toString(), "fechaFin", hasta.get().toString()));
    }
}
