package co.edu.uptc.gui;

import co.edu.uptc.negocio.Cliente;
import co.edu.uptc.negocio.ClienteService;
import co.edu.uptc.negocio.PersistenciaException;
import co.edu.uptc.negocio.SesionService;
import co.edu.uptc.negocio.ValidacionException;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControladorCliente implements ActionListener {

    private final VentanaPrincipal vista;
    private final VentanaCliente ventanaCliente;
    private final PanelCliente panelCliente;
    private final ClienteService servicio;
    private final SesionService sesion;
    private Runnable alCambiarSesion = () -> { };

    public ControladorCliente(VentanaPrincipal vista, VentanaCliente ventanaCliente,
                              ClienteService servicio, SesionService sesion) {
        this.vista = vista;
        this.ventanaCliente = ventanaCliente;
        this.panelCliente = ventanaCliente.getPanelCliente();
        this.servicio = servicio;
        this.sesion = sesion;
        panelCliente.agregarListener(this);
        vista.agregarListenerMenu(this);
        actualizarPanel();
    }

    public void setAlCambiarSesion(Runnable accion) {
        this.alCambiarSesion = accion == null ? () -> { } : accion;
    }

    public void mostrarVentana() {
        actualizarPanel();
        vista.mostrarVentanaInterna(ventanaCliente);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            switch (e.getActionCommand()) {
                case Comandos.CLIENTE_REGISTRAR:
                    registrar();
                    break;
                case Comandos.CLIENTE_ACTUALIZAR:
                    actualizar();
                    break;
                case Comandos.CLIENTE_LOGIN:
                    iniciarSesion();
                    break;
                case Comandos.CLIENTE_LOGOUT:
                    cerrarSesion();
                    break;
                case Comandos.MENU_GESTIONAR_CLIENTES:
                    mostrarVentana();
                    break;
                default:
                    break;
            }
        } catch (ValidacionException ex) {
            UtilidadesGUI.mostrarAdvertencia(vista, ex.getMessage());
        } catch (PersistenciaException ex) {
            UtilidadesGUI.mostrarError(vista, ex.getMessage());
        }
    }

    private void registrar() throws ValidacionException {
        Cliente cliente = servicio.registrarCliente(panelCliente.getNombre(), panelCliente.getCorreo(),
                panelCliente.getDireccion(), panelCliente.getTelefono(), panelCliente.getTipo(),
                panelCliente.getContrasena());
        UtilidadesGUI.mostrarInformacion(vista, "Cliente registrado correctamente.\n"
                + "Ahora puede iniciar sesión con " + cliente.getCorreoElectronico() + ".");
        panelCliente.prepararRegistro();
    }

    private void actualizar() throws ValidacionException {
        sesion.exigirAutenticacion();
        Cliente actual = sesion.getClienteAutenticado();
        Cliente actualizado = servicio.actualizarCliente(actual.getCorreoElectronico(),
                panelCliente.getNombre(), panelCliente.getDireccion(), panelCliente.getTelefono(),
                panelCliente.getTipo(), panelCliente.getContrasena());
        sesion.iniciarSesion(actualizado);
        actualizarPanel();
        alCambiarSesion.run();
        UtilidadesGUI.mostrarInformacion(vista, "Datos del cliente actualizados correctamente.");
    }

    private void iniciarSesion() throws ValidacionException {
        Cliente cliente = servicio.autenticar(panelCliente.getCorreo(), panelCliente.getContrasena());
        sesion.iniciarSesion(cliente);
        actualizarPanel();
        alCambiarSesion.run();
        UtilidadesGUI.mostrarInformacion(vista, "Sesión iniciada correctamente.\n"
                + "Cliente: " + cliente.getNombreCompleto() + "\n"
                + "Tipo: " + cliente.getTipoCliente().getEtiqueta());
    }

    private void cerrarSesion() {
        sesion.cerrarSesion();
        actualizarPanel();
        alCambiarSesion.run();
        UtilidadesGUI.mostrarInformacion(vista, "Sesión cerrada correctamente.");
    }

    private void actualizarPanel() {
        Cliente cliente = sesion.getClienteAutenticado();
        panelCliente.actualizarEstado(cliente);
        if (cliente == null) {
            vista.actualizarEstadoSesion("Usuario: Invitado | Sin sesión | V3");
        } else {
            vista.actualizarEstadoSesion("Usuario: " + cliente.getNombreCompleto()
                    + " | " + cliente.getTipoCliente().getEtiqueta() + " | V3");
        }
    }
}
