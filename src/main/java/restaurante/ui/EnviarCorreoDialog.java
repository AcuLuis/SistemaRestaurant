package restaurante.ui;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingWorker;
import restaurante.correo.ServicioCorreo;
import restaurante.ui.util.Dialogos;
import restaurante.util.Validaciones;

/**
 * Diálogo para enviar un correo con archivos adjuntos (por ejemplo, un
 * reporte en PDF). El envío se hace en segundo plano para no bloquear la
 * interfaz.
 */
public class EnviarCorreoDialog extends JDialog {

    private static final long serialVersionUID = 1L;

    private final JTextField txtPara = new JTextField(30);
    private final JTextField txtAsunto = new JTextField(30);
    private final JTextArea txtMensaje = new JTextArea(6, 30);
    private final DefaultListModel<File> adjuntos = new DefaultListModel<>();
    private final JList<File> lstAdjuntos = new JList<>(adjuntos);
    private final JButton btnEnviar = new JButton("Enviar");

    public EnviarCorreoDialog(Window padre, String asunto, List<File> archivos) {
        super(padre, "Enviar por correo", ModalityType.APPLICATION_MODAL);
        txtAsunto.setText(asunto == null ? "" : asunto);
        archivos.forEach(adjuntos::addElement);
        construir();
        pack();
        setMinimumSize(getSize());
        setLocationRelativeTo(padre);
    }

    /** Abre el diálogo comprobando antes que el correo esté configurado. */
    public static void abrir(Window padre, String asunto, List<File> archivos) {
        if (!ServicioCorreo.configurado()) {
            Dialogos.aviso(padre, "El envío de correos no está configurado.\n\n"
                    + "Complete las claves smtp.host, smtp.user y smtp.password en config/app.properties "
                    + "(con Gmail se necesita una contraseña de aplicación).");
            return;
        }
        new EnviarCorreoDialog(padre, asunto, archivos).setVisible(true);
    }

    private void construir() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        JPanel campos = new JPanel(new GridBagLayout());
        campos.setBorder(BorderFactory.createEmptyBorder(12, 12, 6, 12));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 4, 4, 4);
        c.anchor = GridBagConstraints.NORTHWEST;

        agregarFila(campos, c, 0, "Para:", txtPara);
        agregarFila(campos, c, 1, "Asunto:", txtAsunto);
        txtMensaje.setLineWrap(true);
        txtMensaje.setWrapStyleWord(true);
        agregarFila(campos, c, 2, "Mensaje:", new JScrollPane(txtMensaje));

        lstAdjuntos.setVisibleRowCount(3);
        lstAdjuntos.setCellRenderer((lista, archivo, i, sel, foco) -> {
            JLabel l = new JLabel(archivo.getName());
            l.setOpaque(sel);
            if (sel) {
                l.setBackground(lista.getSelectionBackground());
                l.setForeground(lista.getSelectionForeground());
            }
            return l;
        });
        JPanel panelAdjuntos = new JPanel(new BorderLayout(4, 4));
        panelAdjuntos.add(new JScrollPane(lstAdjuntos), BorderLayout.CENTER);
        JPanel botonesAdjuntos = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        JButton btnAgregar = new JButton("Agregar...");
        btnAgregar.addActionListener(e -> agregarAdjuntos());
        JButton btnQuitar = new JButton("Quitar");
        btnQuitar.addActionListener(e -> lstAdjuntos.getSelectedValuesList().forEach(adjuntos::removeElement));
        botonesAdjuntos.add(btnAgregar);
        botonesAdjuntos.add(btnQuitar);
        panelAdjuntos.add(botonesAdjuntos, BorderLayout.SOUTH);
        agregarFila(campos, c, 3, "Adjuntos:", panelAdjuntos);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());
        btnEnviar.addActionListener(e -> enviar());
        botones.add(btnEnviar);
        botones.add(btnCancelar);

        getContentPane().add(campos, BorderLayout.CENTER);
        getContentPane().add(botones, BorderLayout.SOUTH);
        getRootPane().setDefaultButton(btnEnviar);
        getRootPane().registerKeyboardAction(e -> dispose(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
        setPreferredSize(new Dimension(520, 420));
    }

    private static void agregarFila(JPanel panel, GridBagConstraints c, int fila, String etiqueta, JComponent campo) {
        c.gridy = fila;
        c.gridx = 0;
        c.weightx = 0;
        c.fill = GridBagConstraints.NONE;
        panel.add(new JLabel(etiqueta), c);
        c.gridx = 1;
        c.weightx = 1;
        c.weighty = fila == 2 ? 1 : 0;
        c.fill = fila == 2 ? GridBagConstraints.BOTH : GridBagConstraints.HORIZONTAL;
        panel.add(campo, c);
    }

    private void agregarAdjuntos() {
        JFileChooser selector = new JFileChooser();
        selector.setMultiSelectionEnabled(true);
        if (selector.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            for (File archivo : selector.getSelectedFiles()) {
                if (!adjuntos.contains(archivo)) {
                    adjuntos.addElement(archivo);
                }
            }
        }
    }

    private void enviar() {
        String para = txtPara.getText().trim();
        if (!Validaciones.esCorreo(para)) {
            Dialogos.aviso(this, "Indique una dirección de correo válida.");
            txtPara.requestFocusInWindow();
            return;
        }
        if (Validaciones.vacio(txtAsunto.getText())) {
            Dialogos.aviso(this, "Indique el asunto del correo.");
            txtAsunto.requestFocusInWindow();
            return;
        }
        List<File> archivos = new ArrayList<>();
        for (int i = 0; i < adjuntos.size(); i++) {
            archivos.add(adjuntos.get(i));
        }
        String asunto = txtAsunto.getText().trim();
        String mensaje = txtMensaje.getText();
        btnEnviar.setEnabled(false);
        btnEnviar.setText("Enviando...");
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                ServicioCorreo.enviar(para, asunto, mensaje, archivos);
                return null;
            }

            @Override
            protected void done() {
                setCursor(Cursor.getDefaultCursor());
                btnEnviar.setEnabled(true);
                btnEnviar.setText("Enviar");
                try {
                    get();
                    Dialogos.info(EnviarCorreoDialog.this, "Correo enviado a " + para + ".");
                    dispose();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (ExecutionException e) {
                    Dialogos.error(EnviarCorreoDialog.this, "No se pudo enviar el correo: "
                            + e.getCause().getMessage());
                }
            }
        }.execute();
    }
}
