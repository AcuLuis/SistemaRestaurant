package restaurante.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import restaurante.seguridad.Sesion;
import restaurante.seguridad.TipoUsuario;
import restaurante.servicio.ResumenServicio;
import restaurante.ui.util.Dialogos;
import restaurante.ui.util.Iconos;
import restaurante.ui.util.Tablas;

/**
 * Panel de inicio con el resumen del día: ventas, productos más vendidos,
 * reservas y avisos de existencias bajas. Cada perfil ve lo que le corresponde.
 */
public class FrmInicio extends JInternalFrame {

    private static final long serialVersionUID = 1L;

    private final transient ResumenServicio servicio = new ResumenServicio();
    private final transient TipoUsuario tipo;
    private final JLabel lblVentas = valor();
    private final JLabel lblTotal = valor();
    private final JLabel lblReservas = valor();
    private final JLabel lblOrdenes = valor();
    private final DefaultTableModel modeloVendidos = new DefaultTableModel(
            new String[] {"Producto", "Unidades", "Importe"}, 0);
    private final DefaultTableModel modeloReservas = new DefaultTableModel(
            new String[] {"Hora", "Cliente", "Personas", "Mesa"}, 0);
    private final DefaultTableModel modeloAlertas = new DefaultTableModel(
            new String[] {"Tipo", "Descripción", "Existencias"}, 0);

    public FrmInicio() {
        super("Inicio", true, true, true, true);
        setFrameIcon(Iconos.icono("inicio"));
        tipo = Sesion.actual().map(Sesion::getTipo).orElse(TipoUsuario.TRANSACCIONES);
        boolean ventas = tipo.puedeRegistrarTransacciones() || tipo.puedeVerReportes();

        String hoy = LocalDate.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL));
        JLabel titulo = new JLabel("Resumen de hoy, " + hoy);
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 18f));
        JButton actualizar = new JButton("Actualizar");
        actualizar.addActionListener(e -> cargar());
        JPanel cabecera = new JPanel(new BorderLayout());
        cabecera.add(titulo, BorderLayout.WEST);
        cabecera.add(actualizar, BorderLayout.EAST);

        JPanel indicadores = new JPanel(new GridLayout(1, 0, 10, 0));
        if (ventas) {
            indicadores.add(tarjeta("Ventas de hoy", lblVentas, "venta"));
            indicadores.add(tarjeta("Total facturado", lblTotal, "transacciones"));
        }
        if (tipo.puedeRegistrarTransacciones()) {
            indicadores.add(tarjeta("Reservas pendientes", lblReservas, "reserva"));
        }
        indicadores.add(tarjeta("Producción pendiente", lblOrdenes, "produccion"));

        JPanel tablas = new JPanel(new GridLayout(1, 0, 10, 0));
        if (ventas) {
            tablas.add(seccion("Más vendidos hoy", modeloVendidos));
        }
        if (tipo.puedeRegistrarTransacciones()) {
            tablas.add(seccion("Reservas de hoy", modeloReservas));
        }
        tablas.add(seccion("Existencias por agotarse", modeloAlertas));

        JPanel contenido = new JPanel(new BorderLayout(0, 12));
        contenido.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        contenido.add(cabecera, BorderLayout.NORTH);
        JPanel centro = new JPanel(new BorderLayout(0, 12));
        centro.add(indicadores, BorderLayout.NORTH);
        centro.add(tablas, BorderLayout.CENTER);
        contenido.add(centro, BorderLayout.CENTER);
        setContentPane(contenido);
        setSize(980, 520);
        cargar();
    }

    private static JLabel valor() {
        JLabel l = new JLabel("-", SwingConstants.LEFT);
        l.setFont(l.getFont().deriveFont(Font.BOLD, 24f));
        return l;
    }

    private static JComponent tarjeta(String titulo, JLabel valor, String icono) {
        JPanel panel = new JPanel(new BorderLayout(10, 4));
        Color borde = UIManager.getColor("Component.borderColor");
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borde == null ? Color.LIGHT_GRAY : borde),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)));
        JLabel etiqueta = new JLabel(titulo);
        etiqueta.setForeground(UIManager.getColor("Label.disabledForeground"));
        JPanel textos = new JPanel(new GridLayout(2, 1));
        textos.setOpaque(false);
        textos.add(etiqueta);
        textos.add(valor);
        panel.add(new JLabel(Iconos.icono(icono)), BorderLayout.WEST);
        panel.add(textos, BorderLayout.CENTER);
        return panel;
    }

    private static JComponent seccion(String titulo, DefaultTableModel modelo) {
        JTable tabla = new JTable(modelo);
        Tablas.configurar(tabla);
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(titulo));
        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);
        return panel;
    }

    /** Consulta los datos en segundo plano para no bloquear la ventana. */
    private void cargar() {
        new SwingWorker<Object[], Void>() {
            @Override
            protected Object[] doInBackground() {
                return new Object[] {servicio.ventasDelDia(), servicio.masVendidosDelDia(8), servicio.reservasDeHoy(),
                    servicio.productosPorAgotarse(), servicio.insumosPorAgotarse(), servicio.ordenesPendientes()};
            }

            @Override
            @SuppressWarnings("unchecked")
            protected void done() {
                try {
                    Object[] d = get();
                    ResumenServicio.VentasDelDia v = (ResumenServicio.VentasDelDia) d[0];
                    lblVentas.setText(String.valueOf(v.getVentas()));
                    lblTotal.setText(Tablas.importe(v.getTotal()));
                    modeloVendidos.setRowCount(0);
                    for (ResumenServicio.ProductoVendido p : (java.util.List<ResumenServicio.ProductoVendido>) d[1]) {
                        modeloVendidos.addRow(new Object[] {p.getProducto(), p.getUnidades(), Tablas.importe(p.getImporte())});
                    }
                    java.util.List<restaurante.modelo.Reserva> reservas = (java.util.List<restaurante.modelo.Reserva>) d[2];
                    lblReservas.setText(String.valueOf(reservas.size()));
                    modeloReservas.setRowCount(0);
                    reservas.forEach(r -> modeloReservas.addRow(new Object[] {r.getHora(), r.getCliente(), r.getPersonas(),
                        r.getMesa() == null ? "-" : r.getMesa()}));
                    modeloAlertas.setRowCount(0);
                    for (restaurante.modelo.Producto p : (java.util.List<restaurante.modelo.Producto>) d[3]) {
                        modeloAlertas.addRow(new Object[] {"Producto", p.getDescripProducto(), p.getCantidad()});
                    }
                    for (restaurante.modelo.Insumo i : (java.util.List<restaurante.modelo.Insumo>) d[4]) {
                        modeloAlertas.addRow(new Object[] {"Insumo", i.getDescripInsumo(),
                            Tablas.cantidad(i.getCantidad()) + " " + i.getUnidadMedida()});
                    }
                    lblOrdenes.setText(String.valueOf(d[5]));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (java.util.concurrent.ExecutionException e) {
                    Dialogos.error(FrmInicio.this, e.getCause());
                }
            }
        }.execute();
    }
}
