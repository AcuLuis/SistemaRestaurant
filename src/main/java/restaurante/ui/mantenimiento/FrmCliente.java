package restaurante.ui.mantenimiento;

import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTextField;
import restaurante.dao.ClienteDao;
import restaurante.modelo.Cliente;
import restaurante.ui.base.FormularioMantenimiento;
import restaurante.util.Validaciones;

/**
 * Mantenimiento de clientes. Los clientes con compras no se eliminan: se
 * marcan como inactivos y dejan de ofrecerse en las ventas y reservas.
 */
public class FrmCliente extends FormularioMantenimiento<Cliente> {

    private static final long serialVersionUID = 1L;
    private static final String[] ESTADOS = {"Activo", "Inactivo"};

    private final transient ClienteDao dao = new ClienteDao();
    private final JTextField txtCodigo = new JTextField();
    private final JTextField txtNombres = new JTextField();
    private final JTextField txtApellidos = new JTextField();
    private final JTextField txtDni = new JTextField();
    private final JTextField txtTelefono = new JTextField();
    private final JTextField txtCorreo = new JTextField();
    private final JTextField txtDireccion = new JTextField();
    private final JComboBox<String> cmbEstado = new JComboBox<>(ESTADOS);

    public FrmCliente() {
        super("Clientes", "cliente", null, "Código", "Apellidos", "Nombres", "DNI", "Teléfono", "Correo",
                "Dirección", "Estado");
        txtCodigo.setEditable(false);
        txtDni.setToolTipText("8 dígitos (opcional)");
        txtTelefono.setToolTipText("Entre 6 y 15 dígitos, puede empezar con + (opcional)");
        agregarCampo("Código:", txtCodigo);
        agregarCampo("Nombres:", txtNombres);
        agregarCampo("Apellidos:", txtApellidos);
        agregarCampo("DNI:", txtDni);
        agregarCampo("Teléfono:", txtTelefono);
        agregarCampo("Correo:", txtCorreo);
        agregarCampo("Dirección:", txtDireccion);
        agregarCampo("Estado:", cmbEstado);
        iniciar();
    }

    @Override
    protected List<Cliente> consultar(String texto) {
        return dao.listar(!texto.isEmpty(), texto);
    }

    @Override
    protected Object[] fila(Cliente c) {
        return new Object[] {c.getClienteID(), c.getApellidos(), c.getNombres(), nulo(c.getDni()), nulo(c.getTelefono()),
            nulo(c.getCorreo()), nulo(c.getDireccion()), c.getEstado() == ClienteDao.ESTADO_ACTIVO ? ESTADOS[0] : ESTADOS[1]};
    }

    @Override
    protected void mostrar(Cliente c) {
        txtCodigo.setText(String.valueOf(c.getClienteID()));
        txtNombres.setText(c.getNombres());
        txtApellidos.setText(c.getApellidos());
        txtDni.setText(nulo(c.getDni()));
        txtTelefono.setText(nulo(c.getTelefono()));
        txtCorreo.setText(nulo(c.getCorreo()));
        txtDireccion.setText(nulo(c.getDireccion()));
        cmbEstado.setSelectedIndex(c.getEstado() == ClienteDao.ESTADO_ACTIVO ? 0 : 1);
    }

    @Override
    protected void limpiarCampos() {
        txtCodigo.setText("(nuevo)");
        for (JTextField t : new JTextField[] {txtNombres, txtApellidos, txtDni, txtTelefono, txtCorreo, txtDireccion}) {
            t.setText("");
        }
        cmbEstado.setSelectedIndex(0);
    }

    @Override
    protected boolean grabar(Cliente seleccionado) {
        String nombres = txtNombres.getText().trim();
        String apellidos = txtApellidos.getText().trim();
        String dni = txtDni.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String correo = txtCorreo.getText().trim();
        String direccion = txtDireccion.getText().trim();
        if (nombres.isEmpty() || nombres.length() > 40) {
            return invalido(txtNombres, "Indique los nombres (máximo 40 caracteres).");
        }
        if (apellidos.isEmpty() || apellidos.length() > 40) {
            return invalido(txtApellidos, "Indique los apellidos (máximo 40 caracteres).");
        }
        if (!dni.isEmpty() && !Validaciones.esDni(dni)) {
            return invalido(txtDni, "El DNI debe tener 8 dígitos.");
        }
        if (!telefono.isEmpty() && !Validaciones.esTelefono(telefono)) {
            return invalido(txtTelefono, "El teléfono debe tener entre 6 y 15 dígitos (puede empezar con +).");
        }
        if (!correo.isEmpty() && (!Validaciones.esCorreo(correo) || correo.length() > 100)) {
            return invalido(txtCorreo, "Indique un correo electrónico válido.");
        }
        if (direccion.length() > 100) {
            return invalido(txtDireccion, "La dirección admite como máximo 100 caracteres.");
        }
        Cliente c = new Cliente();
        c.setNombres(nombres);
        c.setApellidos(apellidos);
        c.setDni(dni);
        c.setTelefono(telefono);
        c.setCorreo(correo);
        c.setDireccion(direccion);
        c.setEstado(cmbEstado.getSelectedIndex() == 0 ? ClienteDao.ESTADO_ACTIVO : ClienteDao.ESTADO_INACTIVO);
        if (seleccionado == null) {
            dao.insertar(c);
        } else {
            c.setClienteID(seleccionado.getClienteID());
            dao.actualizar(c);
        }
        return true;
    }

    @Override
    protected String describir(Cliente c) {
        return "al cliente " + c.getNombres() + " " + c.getApellidos();
    }

    @Override
    protected JComponent primerCampo() {
        return txtNombres;
    }

    private static String nulo(String texto) {
        return texto == null ? "" : texto;
    }
}
