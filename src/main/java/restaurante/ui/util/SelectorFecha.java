package restaurante.ui.util;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import restaurante.util.Validaciones;

/**
 * Añade a un campo de texto de fecha un botón que abre un calendario. El
 * campo sigue admitiendo escribir la fecha con el formato aaaa-mm-dd.
 */
public final class SelectorFecha {

    private static final Locale ES = Locale.forLanguageTag("es");

    private final JTextField campo;
    private final JPopupMenu popup = new JPopupMenu();
    private final JLabel titulo = new JLabel("", SwingConstants.CENTER);
    private final JPanel dias = new JPanel(new GridLayout(0, 7, 2, 2));
    private YearMonth mes;

    private SelectorFecha(JTextField campo) {
        this.campo = campo;
        JButton anterior = boton("<", e -> mostrarMes(mes.minusMonths(1)));
        JButton siguiente = boton(">", e -> mostrarMes(mes.plusMonths(1)));
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD));
        JPanel cabecera = new JPanel(new BorderLayout());
        cabecera.add(anterior, BorderLayout.WEST);
        cabecera.add(titulo, BorderLayout.CENTER);
        cabecera.add(siguiente, BorderLayout.EAST);
        JButton hoy = new JButton("Hoy");
        hoy.addActionListener(e -> elegir(LocalDate.now()));
        JPanel panel = new JPanel(new BorderLayout(4, 4));
        panel.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        panel.add(cabecera, BorderLayout.NORTH);
        panel.add(dias, BorderLayout.CENTER);
        panel.add(hoy, BorderLayout.SOUTH);
        popup.add(panel);
    }

    /** Instala el calendario en el campo (indica también el formato esperado). */
    public static void instalar(JTextField campo) {
        SelectorFecha selector = new SelectorFecha(campo);
        JButton abrir = new JButton(Iconos.icono("calendario"));
        abrir.setToolTipText("Elegir fecha en el calendario");
        abrir.setMargin(new Insets(0, 2, 0, 2));
        abrir.setFocusable(false);
        abrir.putClientProperty("JButton.buttonType", "toolBarButton");
        abrir.addActionListener(e -> selector.abrir(abrir));
        campo.putClientProperty("JTextField.trailingComponent", abrir);
        campo.putClientProperty("JTextField.placeholderText", Validaciones.FORMATO_FECHA);
        if (campo.getToolTipText() == null) {
            campo.setToolTipText("Formato " + Validaciones.FORMATO_FECHA + " o elija la fecha en el calendario");
        }
    }

    private void abrir(java.awt.Component origen) {
        LocalDate actual = Validaciones.fecha(campo.getText()).orElse(LocalDate.now());
        mostrarMes(YearMonth.from(actual));
        int x = Math.min(0, campo.getWidth() - popup.getPreferredSize().width);
        popup.show(campo, x, campo.getHeight());
    }

    private void mostrarMes(YearMonth nuevo) {
        mes = nuevo;
        String nombre = mes.getMonth().getDisplayName(TextStyle.FULL_STANDALONE, ES);
        titulo.setText(Character.toUpperCase(nombre.charAt(0)) + nombre.substring(1) + " " + mes.getYear());
        dias.removeAll();
        for (DayOfWeek d = DayOfWeek.MONDAY; ; d = d.plus(1)) {
            JLabel l = new JLabel(d.getDisplayName(TextStyle.SHORT_STANDALONE, ES).substring(0, 2), SwingConstants.CENTER);
            l.setEnabled(false);
            dias.add(l);
            if (d == DayOfWeek.SUNDAY) {
                break;
            }
        }
        int huecos = mes.atDay(1).getDayOfWeek().getValue() - 1;
        for (int i = 0; i < huecos; i++) {
            dias.add(new JLabel());
        }
        LocalDate hoy = LocalDate.now();
        LocalDate elegida = Validaciones.fecha(campo.getText()).orElse(null);
        for (int dia = 1; dia <= mes.lengthOfMonth(); dia++) {
            LocalDate fecha = mes.atDay(dia);
            JButton b = boton(String.valueOf(dia), e -> elegir(fecha));
            b.setPreferredSize(new Dimension(36, 26));
            if (fecha.equals(elegida)) {
                b.setFont(b.getFont().deriveFont(Font.BOLD));
                b.putClientProperty("JButton.buttonType", null);
            } else if (fecha.equals(hoy)) {
                b.setFont(b.getFont().deriveFont(Font.BOLD));
            }
            dias.add(b);
        }
        dias.revalidate();
        popup.pack();
    }

    private void elegir(LocalDate fecha) {
        campo.setText(fecha.toString());
        popup.setVisible(false);
        campo.requestFocusInWindow();
    }

    private static JButton boton(String texto, java.awt.event.ActionListener accion) {
        JButton b = new JButton(texto);
        b.setMargin(new Insets(1, 2, 1, 2));
        b.setFocusable(false);
        b.putClientProperty("JButton.buttonType", "toolBarButton");
        b.addActionListener(accion);
        return b;
    }
}
