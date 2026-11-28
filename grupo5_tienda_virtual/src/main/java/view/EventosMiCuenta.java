package view;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

import model.Cliente;
import service.IServicioCarrito;
import service.IServicioClientes;
import service.IServicioCompras;
import service.IServicioLibros;

/**
 * Manejador de eventos de la ventana Mi Cuenta.
 */
public class EventosMiCuenta extends Eventos {

    private final IServicioClientes controlador;
    private final IServicioLibros controladorLibro;
    private final IServicioCompras controladorCompras;
    private final IServicioCarrito controladorCarrito;
    private final Cliente cliente;

    public EventosMiCuenta(VentanaMiCuenta vista, IServicioClientes controlador,
            IServicioLibros controladorLibro, IServicioCompras controladorCompras,
            IServicioCarrito controladorCarrito, Cliente cliente) {
        super(vista);
        this.controlador = controlador;
        this.controladorLibro = controladorLibro;
        this.controladorCompras = controladorCompras;
        this.controladorCarrito = controladorCarrito;
        this.cliente = cliente;
    }

    public void registrarEventos(JTextField txtNombre, JTextField txtCorreo,
            JTextField txtDireccion, JTextField txtTelefono,
            JButton btnAtras, JButton btnGuardar,
            JButton btnCompras, JButton btnHistorial, JFrame padre) {
        btnAtras.addActionListener(e -> volver(padre));
        btnGuardar.addActionListener(e -> guardarCambios(txtNombre, txtDireccion, txtTelefono));
        btnCompras.addActionListener(e -> abrirCompras());
        btnHistorial.addActionListener(e -> abrirHistorial());

        ventana.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (padre != null) {
                    padre.setVisible(true);
                }
            }
        });

        cargarDatos(txtNombre, txtCorreo, txtDireccion, txtTelefono);
    }

    private void cargarDatos(JTextField txtNombre, JTextField txtCorreo,
            JTextField txtDireccion, JTextField txtTelefono) {
        if (cliente == null) {
            return;
        }
        txtNombre.setText(cliente.getNombreCompleto());
        txtCorreo.setText(cliente.getCorreo());
        txtDireccion.setText(cliente.getDireccion());
        txtTelefono.setText(cliente.getTelefono());
    }

    private void guardarCambios(JTextField txtNombre, JTextField txtDireccion,
            JTextField txtTelefono) {
        if (cliente == null) {
            mostrarError("No hay sesi\u00f3n iniciada.");
            return;
        }

        String nombre = txtNombre.getText().trim();
        String direccion = txtDireccion.getText().trim();
        String telefono = txtTelefono.getText().trim();

        if (nombre.isEmpty()) {
            mostrarError("El nombre completo es obligatorio.");
            return;
        }

        if (controlador.actualizarDatos(cliente.getCorreo(), nombre, direccion, telefono)) {
            mostrarInfo("Datos actualizados correctamente.");
        } else {
            mostrarError("No se pudieron actualizar los datos. Verifique el almacenamiento.");
        }
    }

    private void abrirCompras() {
        new VentanaCompras(cliente, controladorLibro, controladorCompras,
                controladorCarrito).setVisible(true);
    }

    private void abrirHistorial() {
        new VentanaHistorialCompras(controladorCompras, cliente, ventana).setVisible(true);
    }
}
