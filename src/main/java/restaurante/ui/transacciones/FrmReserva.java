package restaurante.ui.transacciones;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTextField;
import restaurante.dao.ClienteDao;
import restaurante.dao.MesaDao;
import restaurante.dao.ReservaDao;
import restaurante.modelo.Cliente;
import restaurante.modelo.Mesa;
import restaurante.modelo.Reserva;
import restaurante.servicio.ReservaServicio;
import restaurante.ui.base.FormularioMantenimiento;
import restaurante.ui.util.SelectorFecha;
import restaurante.util.Validaciones;

/** Reservas de mesa. */
public class FrmReserva extends FormularioMantenimiento<Reserva> {

    private static final long serialVersionUID = 1L;
    private static final String[] ESTADOS = {"Cancelada", "Pendiente", "Atendida"};

    private final transient ReservaDao dao = new ReservaDao();
    private final transient ReservaServicio servicio = new ReservaServicio();
    private final transient List<Cliente> clientes = new ArrayList<>(new ClienteDao().listarActivos(false, ""));
    private final transient List<Mesa> mesas = new ArrayList<>(new MesaDao().listarDisponibles());
    private final JTextField txtCodigo = new JTextField();
    private final JComboBox<String> cmbCliente = new JComboBox<>();
    private final JTextField txtFecha = new JTextField();
    private final JTextField txtHora = new JTextField();
    private final JTextField txtPersonas = new JTextField();
    private final JComboBox<String> cmbMesa = new JComboBox<>();
    private final JComboBox<String> cmbEstado = new JComboBox<>(ESTADOS);
    private final JTextField txtObservacion = new JTextField();

    public FrmReserva() {
        super("Reservas", "reserva", "Eliminar", "N.º", "Fecha", "Hora", "Cliente", "Personas", "Mesa", "Estado");
        txtCodigo.setEditable(false);
        clientes.forEach(c -> cmbCliente.addItem(nombre(c)));
        cmbMesa.addItem("(sin asignar)");
        mesas.forEach(m -> cmbMesa.addItem(m.getUbic() + " (" + m.getNumSillas() + " sillas)"));
        SelectorFecha.instalar(txtFecha);
        txtHora.putClientProperty("JTextField.placeholderText", "hh:mm");
        txtHora.setToolTipText("Hora con el formato hh:mm, por ejemplo 20:30");
        agregarCampo("N.º:", txtCodigo);
        agregarCampo("Cliente:", cmbCliente);
        agregarCampo("Fecha:", txtFecha);
        agregarCampo("Hora:", txtHora);
        agregarCampo("Personas:", txtPersonas);
        agregarCampo("Mesa:", cmbMesa);
        agregarCampo("Estado:", cmbEstado);
        agregarCampo("Observación:", txtObservacion);
        iniciar();
    }

    @Override
    protected List<Reserva> consultar(String texto) {
        return dao.listar(!texto.isEmpty(), texto);
    }

    @Override
    protected Object[] fila(Reserva r) {
        return new Object[] {r.getReservaID(), r.getFecha(), r.getHora(), r.getCliente(), r.getPersonas(),
            r.getMesa() == null ? "-" : r.getMesa(), ESTADOS[r.getEstado()]};
    }

    @Override
    protected void mostrar(Reserva r) {
        txtCodigo.setText(String.valueOf(r.getReservaID()));
        int cliente = indiceCliente(r.getClienteID());
        if (cliente < 0) {
            Cliente c = new Cliente();
            c.setClienteID(r.getClienteID());
            String[] partes = r.getCliente().split(", ", 2);
            c.setApellidos(partes[0]);
            c.setNombres(partes.length > 1 ? partes[1] : "");
            clientes.add(c);
            cmbCliente.addItem(nombre(c));
            cliente = clientes.size() - 1;
        }
        cmbCliente.setSelectedIndex(cliente);
        txtFecha.setText(r.getFecha());
        txtHora.setText(r.getHora());
        txtPersonas.setText(String.valueOf(r.getPersonas()));
        int mesa = r.getMesaID() == null ? -1 : indiceMesa(r.getMesaID());
        if (r.getMesaID() != null && mesa < 0) {
            Mesa m = new Mesa();
            m.setMesaID(r.getMesaID());
            m.setUbic(r.getMesa());
            mesas.add(m);
            cmbMesa.addItem(r.getMesa());
            mesa = mesas.size() - 1;
        }
        cmbMesa.setSelectedIndex(mesa + 1);
        cmbEstado.setSelectedIndex(r.getEstado());
        txtObservacion.setText(r.getObservacion() == null ? "" : r.getObservacion());
    }

    @Override
    protected void limpiarCampos() {
        txtCodigo.setText("(nuevo)");
        cmbCliente.setSelectedIndex(clientes.isEmpty() ? -1 : 0);
        txtFecha.setText(LocalDate.now().toString());
        txtHora.setText("");
        txtPersonas.setText("2");
        cmbMesa.setSelectedIndex(0);
        cmbEstado.setSelectedIndex(Reserva.PENDIENTE);
        txtObservacion.setText("");
    }

    @Override
    protected boolean grabar(Reserva seleccionado) {
        if (cmbCliente.getSelectedIndex() < 0) {
            return invalido(cmbCliente, "Seleccione el cliente (regístrelo antes en Clientes si es nuevo).");
        }
        Optional<Integer> personas = Validaciones.enteroPositivo(txtPersonas.getText());
        if (personas.isEmpty()) {
            return invalido(txtPersonas, "El número de personas debe ser un entero mayor que cero.");
        }
        Reserva r = new Reserva();
        r.setReservaID(seleccionado == null ? 0 : seleccionado.getReservaID());
        r.setClienteID(clientes.get(cmbCliente.getSelectedIndex()).getClienteID());
        r.setFecha(txtFecha.getText().trim());
        r.setHora(txtHora.getText().trim());
        r.setPersonas(personas.get());
        r.setMesaID(cmbMesa.getSelectedIndex() <= 0 ? null : mesas.get(cmbMesa.getSelectedIndex() - 1).getMesaID());
        r.setEstado(cmbEstado.getSelectedIndex());
        String observacion = txtObservacion.getText().trim();
        r.setObservacion(observacion.isEmpty() ? null : observacion);
        servicio.guardar(r);
        return true;
    }

    @Override
    protected void eliminar(Reserva r) {
        dao.eliminar(r);
    }

    @Override
    protected String describir(Reserva r) {
        return "la reserva N.º " + r.getReservaID() + " de " + r.getCliente();
    }

    @Override
    protected JComponent primerCampo() {
        return cmbCliente;
    }

    private int indiceCliente(int clienteID) {
        for (int i = 0; i < clientes.size(); i++) {
            if (clientes.get(i).getClienteID() == clienteID) {
                return i;
            }
        }
        return -1;
    }

    private int indiceMesa(int mesaID) {
        for (int i = 0; i < mesas.size(); i++) {
            if (mesas.get(i).getMesaID() == mesaID) {
                return i;
            }
        }
        return -1;
    }

    private static String nombre(Cliente c) {
        return c.getApellidos() + ", " + c.getNombres();
    }
}
