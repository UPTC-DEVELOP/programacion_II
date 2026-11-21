package view;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import model.Cliente;
import service.IServicioClientes;
import service.IServicioCompras;
import service.IServicioLibros;

/**
 * Manejador de eventos de la ventana de inicio de sesion.
 */
public class EventosLogin extends Eventos {

    private final IServicioClientes controlador;
    private final IServicioLibros controladorLibro;
    private final IServicioCompras controladorCompras;

    public EventosLogin(VentanaLogin vista, IServicioClientes controlador,
            IServicioLibros controladorLibro, IServicioCompras controladorCompras) {
        super(vista);
        this.controlador = controlador;
        this.controladorLibro = controladorLibro;
        this.controladorCompras = controladorCompras;
    }

    public void registrarEventos(JTextField txtCorreo, JPasswordField txtContrasena,
            JButton btnIniciarSesion, JButton btnRegistrarse, JButton btnAdministrador) {
        btnIniciarSesion.addActionListener(e -> iniciarSesion(txtCorreo, txtContrasena));
        btnRegistrarse.addActionListener(e -> abrirRegistro());
        btnAdministrador.addActionListener(e -> abrirAdministracion());
    }

    private void iniciarSesion(JTextField txtCorreo, JPasswordField txtContrasena) {
        String correo = txtCorreo.getText().trim();
        String contrasena = new String(txtContrasena.getPassword());

        if (correo.isEmpty() || contrasena.isEmpty()) {
            mostrarError("Correo y contrase\u00f1a obligatorios.");
            return;
        }

        Cliente cliente = controlador.iniciarSesion(correo, contrasena);
        if (cliente != null) {
            new VentanaMiCuenta(controlador, controladorLibro, controladorCompras, cliente, ventana)
                    .setVisible(true);
            ventana.setVisible(false);
        } else {
            mostrarError("Correo o contrase\u00f1a no v\u00e1lidos.");
        }
    }

    private void abrirRegistro() {
        new VentanaRegistroCliente(controlador, ventana).setVisible(true);
        ventana.setVisible(false);
    }

    private void abrirAdministracion() {
        Object[] opciones = { "Gesti\u00f3n de Libros", "Gesti\u00f3n de Clientes" };
        int opcion = JOptionPane.showOptionDialog(ventana,
                "Seleccione el m\u00f3dulo de administraci\u00f3n:",
                "Administraci\u00f3n",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]);

        if (opcion == 0) {
            new VentanaLibros(ventana, controladorLibro).setVisible(true);
            ventana.setVisible(false);
        } else if (opcion == 1) {
            new VentanaClientes(controlador, ventana).setVisible(true);
            ventana.setVisible(false);
        }
    }
}
