package restaurante.ui.mantenimiento;

import java.util.List;
import java.util.Optional;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTextField;
import restaurante.dao.MesaDao;
import restaurante.modelo.Mesa;
import restaurante.ui.base.FormularioMantenimiento;
import restaurante.util.Validaciones;

/**
 * Mantenimiento de las mesas del local. Una mesa fuera de servicio no se
 * ofrece en las ventas ni en las reservas.
 */
public class FrmMesa extends FormularioMantenimiento<Mesa> {

    private static final long serialVersionUID = 1L;
    private static final String[] ESTADOS = {"Disponible", "Fuera de servicio"};

    private final transient MesaDao dao = new MesaDao();
    private final JTextField txtCodigo = new JTextField();
    private final JTextField txtUbicacion = new JTextField();
    private final JTextField txtSillas = new JTextField();
    private final JComboBox<String> cmbEstado = new JComboBox<>(ESTADOS);

    public FrmMesa() {
        super("Mesas", "mesa", "Eliminar", "Código", "Ubicación", "Sillas", "Estado");
        txtCodigo.setEditable(false);
        txtUbicacion.setToolTipText("Por ejemplo: Salón - Mesa 6 (máximo 30 caracteres)");
        agregarCampo("Código:", txtCodigo);
        agregarCampo("Ubicación:", txtUbicacion);
        agregarCampo("N.º de sillas:", txtSillas);
        agregarCampo("Estado:", cmbEstado);
        setSize(760, 420);
        iniciar();
    }

    @Override
    protected List<Mesa> consultar(String texto) {
        return dao.listar(!texto.isEmpty(), texto);
    }

    @Override
    protected Object[] fila(Mesa m) {
        return new Object[] {m.getMesaID(), m.getUbic(), m.getNumSillas(),
            m.getEstado() == MesaDao.ESTADO_DISPONIBLE ? ESTADOS[0] : ESTADOS[1]};
    }

    @Override
    protected void mostrar(Mesa m) {
        txtCodigo.setText(String.valueOf(m.getMesaID()));
        txtUbicacion.setText(m.getUbic());
        txtSillas.setText(String.valueOf(m.getNumSillas()));
        cmbEstado.setSelectedIndex(m.getEstado() == MesaDao.ESTADO_DISPONIBLE ? 0 : 1);
    }

    @Override
    protected void limpiarCampos() {
        txtCodigo.setText("(nuevo)");
        txtUbicacion.setText("");
        txtSillas.setText("");
        cmbEstado.setSelectedIndex(0);
    }

    @Override
    protected boolean grabar(Mesa seleccionado) {
        String ubicacion = txtUbicacion.getText().trim();
        Optional<Integer> sillas = Validaciones.enteroPositivo(txtSillas.getText());
        if (ubicacion.isEmpty() || ubicacion.length() > 30) {
            return invalido(txtUbicacion, "Indique la ubicación de la mesa (máximo 30 caracteres).");
        }
        if (sillas.isEmpty() || sillas.get() > 50) {
            return invalido(txtSillas, "El número de sillas debe ser un entero entre 1 y 50.");
        }
        Mesa m = new Mesa();
        m.setUbic(ubicacion);
        m.setNumSillas(sillas.get());
        m.setEstado(cmbEstado.getSelectedIndex() == 0 ? MesaDao.ESTADO_DISPONIBLE : MesaDao.ESTADO_FUERA_DE_SERVICIO);
        if (seleccionado == null) {
            dao.insertar(m);
        } else {
            m.setMesaID(seleccionado.getMesaID());
            dao.actualizar(m);
        }
        return true;
    }

    @Override
    protected void eliminar(Mesa m) {
        dao.eliminar(m);
    }

    @Override
    protected String describir(Mesa m) {
        return "la mesa \"" + m.getUbic() + "\"";
    }

    @Override
    protected JComponent primerCampo() {
        return txtUbicacion;
    }
}
