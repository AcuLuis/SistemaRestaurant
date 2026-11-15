package restaurante.ui.mantenimiento;

import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTextField;
import restaurante.dao.EmpleadoDao;
import restaurante.modelo.Empleado;
import restaurante.servicio.EmpleadoServicio;
import restaurante.ui.base.FormularioMantenimiento;
import restaurante.ui.util.SelectorFecha;

/**
 * Mantenimiento de empleados. Los empleados no se eliminan: se dan de baja
 * para conservar sus contratos y ventas.
 */
public class FrmEmpleado extends FormularioMantenimiento<Empleado> {

    private static final long serialVersionUID = 1L;
    private static final String[] GENEROS = {"Masculino", "Femenino"};
    private static final String[] ESTADOS_CIVILES = {"SOLTERO", "CASADO", "DIVORCIADO", "VIUDO"};
    private static final String[] ESTADOS = {"Activo", "De baja"};

    private final transient EmpleadoDao dao = new EmpleadoDao();
    private final transient EmpleadoServicio servicio = new EmpleadoServicio();
    private final JTextField txtCodigo = new JTextField();
    private final JTextField txtNombres = new JTextField();
    private final JTextField txtApellidos = new JTextField();
    private final JTextField txtDni = new JTextField();
    private final JTextField txtNacimiento = new JTextField();
    private final JComboBox<String> cmbGenero = new JComboBox<>(GENEROS);
    private final JComboBox<String> cmbEstadoCivil = new JComboBox<>(ESTADOS_CIVILES);
    private final JComboBox<String> cmbEstado = new JComboBox<>(ESTADOS);

    public FrmEmpleado() {
        super("Empleados", "empleado", null, "Código", "Nombres", "Apellidos", "DNI", "Nacimiento", "Género",
                "Estado civil", "Estado");
        txtCodigo.setEditable(false);
        txtDni.setToolTipText("8 dígitos");
        SelectorFecha.instalar(txtNacimiento);
        agregarCampo("Código:", txtCodigo);
        agregarCampo("Nombres:", txtNombres);
        agregarCampo("Apellidos:", txtApellidos);
        agregarCampo("DNI:", txtDni);
        agregarCampo("Fecha de nacimiento:", txtNacimiento);
        agregarCampo("Género:", cmbGenero);
        agregarCampo("Estado civil:", cmbEstadoCivil);
        agregarCampo("Estado:", cmbEstado);
        iniciar();
    }

    @Override
    protected List<Empleado> consultar(String texto) {
        return dao.listar(!texto.isEmpty(), texto);
    }

    @Override
    protected Object[] fila(Empleado e) {
        return new Object[] {e.getEmpleadoID(), e.getNombres(), e.getApellidos(), e.getDni(), e.getFechaNacimiento(),
            e.getGenero() == 1 ? GENEROS[0] : GENEROS[1], e.getEstadoCivil(),
            e.getEstado() == EmpleadoDao.ESTADO_ACTIVO ? ESTADOS[0] : ESTADOS[1]};
    }

    @Override
    protected void mostrar(Empleado e) {
        txtCodigo.setText(String.valueOf(e.getEmpleadoID()));
        txtNombres.setText(e.getNombres());
        txtApellidos.setText(e.getApellidos());
        txtDni.setText(e.getDni() == null ? "" : e.getDni());
        txtNacimiento.setText(e.getFechaNacimiento() == null ? "" : e.getFechaNacimiento());
        cmbGenero.setSelectedIndex(e.getGenero() == 1 ? 0 : 1);
        cmbEstadoCivil.setSelectedItem(e.getEstadoCivil());
        cmbEstado.setSelectedIndex(e.getEstado() == EmpleadoDao.ESTADO_ACTIVO ? 0 : 1);
    }

    @Override
    protected void limpiarCampos() {
        txtCodigo.setText("(nuevo)");
        txtNombres.setText("");
        txtApellidos.setText("");
        txtDni.setText("");
        txtNacimiento.setText("");
        cmbGenero.setSelectedIndex(0);
        cmbEstadoCivil.setSelectedIndex(0);
        cmbEstado.setSelectedIndex(0);
    }

    @Override
    protected boolean grabar(Empleado seleccionado) {
        Empleado e = new Empleado();
        e.setEmpleadoID(seleccionado == null ? 0 : seleccionado.getEmpleadoID());
        e.setNombres(txtNombres.getText().trim());
        e.setApellidos(txtApellidos.getText().trim());
        e.setDni(txtDni.getText().trim());
        e.setFechaNacimiento(txtNacimiento.getText().trim());
        // Se conserva la codificación original: 1 = masculino, 0 = femenino.
        e.setGenero(cmbGenero.getSelectedIndex() == 0 ? 1 : 0);
        e.setEstadoCivil((String) cmbEstadoCivil.getSelectedItem());
        e.setEstado(cmbEstado.getSelectedIndex() == 0 ? EmpleadoDao.ESTADO_ACTIVO : EmpleadoDao.ESTADO_BAJA);
        servicio.guardar(e);
        return true;
    }

    @Override
    protected String describir(Empleado e) {
        return "al empleado " + e.getNombres() + " " + e.getApellidos();
    }

    @Override
    protected JComponent primerCampo() {
        return txtNombres;
    }
}
