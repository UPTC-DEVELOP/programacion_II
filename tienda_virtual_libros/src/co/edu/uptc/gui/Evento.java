package co.edu.uptc.gui;

import co.edu.uptc.excepciones.ReglaNegocioException;
import co.edu.uptc.modelo.dto.ClienteDto;
import co.edu.uptc.modelo.dto.CredencialDto;
import co.edu.uptc.modelo.dto.FiltroLibroDto;
import co.edu.uptc.modelo.dto.LibroDto;
import co.edu.uptc.modelo.enums.Rol;
import co.edu.uptc.modelo.mapper.ClienteMapper;
import co.edu.uptc.negocio.GestionCliente;
import co.edu.uptc.negocio.GestionLibro;
import co.edu.uptc.negocio.GestionSeguridad;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Clase centralizada para manejar todos los eventos de la aplicación
 * (patrón Controller / Observer).
 *
 * PRINCIPIO SOLID aplicado:
 * - SRP: Esta clase SOLO traduce eventos de la GUI en llamadas a la capa de
 *   negocio; no valida reglas ni persiste datos.
 * - DIP: Depende de las clases de negocio (GestionSeguridad, GestionCliente,
 *   GestionLibro), nunca de la persistencia concreta.
 *
 * @author Grupo 7
 * @version 1.0
 */
public class Evento implements ActionListener {

    // --- Comandos de autenticación y navegación ---
    public static final String CMD_LOGIN = "INICIAR_SESION";
    public static final String CMD_LOGOUT = "CERRAR_SESION";

    // --- CRUD de clientes (panel del administrador) ---
    public static final String CMD_REGISTRAR_CLIENTE = "REGISTRAR_CLIENTE"; // desde el login
    public static final String CMD_NUEVO_CLIENTE = "NUEVO_CLIENTE";
    public static final String CMD_EDITAR_CLIENTE = "EDITAR_CLIENTE";
    public static final String CMD_ELIMINAR_CLIENTE = "ELIMINAR_CLIENTE";
    public static final String CMD_BUSCAR_CLIENTE = "BUSCAR_CLIENTE";

    // --- CRUD de libros (panel del administrador) ---
    public static final String CMD_NUEVO_LIBRO = "NUEVO_LIBRO";
    public static final String CMD_EDITAR_LIBRO = "EDITAR_LIBRO";
    public static final String CMD_ELIMINAR_LIBRO = "ELIMINAR_LIBRO";
    public static final String CMD_BUSCAR_LIBRO = "BUSCAR_LIBRO";

    // --- CRUD del perfil del cliente (panel del cliente) ---
    public static final String CMD_EDITAR_PERFIL = "EDITAR_MI_PERFIL";
    public static final String CMD_ELIMINAR_CUENTA = "ELIMINAR_MI_CUENTA";
    public static final String CMD_BUSCAR_CATALOGO = "BUSCAR_CATALOGO";

    private final VentanaPrincipal ventanaPrincipal;
    private final GestionSeguridad gestionSeguridad;
    private final GestionCliente gestionCliente;
    private final GestionLibro gestionLibro;

    public Evento(VentanaPrincipal ventanaPrincipal, GestionSeguridad gestionSeguridad,
                  GestionCliente gestionCliente, GestionLibro gestionLibro) {
        this.ventanaPrincipal = ventanaPrincipal;
        this.gestionSeguridad = gestionSeguridad;
        this.gestionCliente = gestionCliente;
        this.gestionLibro = gestionLibro;
    }

    /**
     * Maneja todos los eventos de la aplicación usando getActionCommand().
     *
     * @param e El evento de acción generado por componentes Swing
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand();

        switch (comando) {
            // ---------------- Autenticación ----------------
            case CMD_LOGIN:
                manejarLogin();
                break;
            case CMD_LOGOUT:
                manejarLogout();
                break;
            case CMD_REGISTRAR_CLIENTE:
            case CMD_NUEVO_CLIENTE:
                manejarNuevoCliente();
                break;

            // ---------------- CRUD de clientes ----------------
            case CMD_EDITAR_CLIENTE:
                manejarEditarCliente();
                break;
            case CMD_ELIMINAR_CLIENTE:
                manejarEliminarCliente();
                break;
            case CMD_BUSCAR_CLIENTE:
                refrescarTablaClientes(ventanaPrincipal.getPanelGestionClientes().getTextoBusqueda());
                break;

            // ---------------- CRUD de libros ----------------
            case CMD_NUEVO_LIBRO:
                manejarNuevoLibro();
                break;
            case CMD_EDITAR_LIBRO:
                manejarEditarLibro();
                break;
            case CMD_ELIMINAR_LIBRO:
                manejarEliminarLibro();
                break;
            case CMD_BUSCAR_LIBRO:
                refrescarTablaLibros(ventanaPrincipal.getPanelGestionLibros().getFiltro());
                break;

            // ---------------- Perfil del cliente ----------------
            case CMD_EDITAR_PERFIL:
                manejarEditarPerfil();
                break;
            case CMD_ELIMINAR_CUENTA:
                manejarEliminarCuenta();
                break;
            case CMD_BUSCAR_CATALOGO:
                refrescarCatalogoCliente();
                break;

            // ---------------- Navegación entre tarjetas ----------------
            case VentanaPrincipal.PANEL_ADMIN:
                ventanaPrincipal.cambiarPanel(VentanaPrincipal.PANEL_ADMIN);
                break;
            case VentanaPrincipal.PANEL_LIBROS:
                refrescarTablaLibros(null);
                ventanaPrincipal.cambiarPanel(VentanaPrincipal.PANEL_LIBROS);
                break;
            case VentanaPrincipal.PANEL_CLIENTES:
                refrescarTablaClientes(null);
                ventanaPrincipal.cambiarPanel(VentanaPrincipal.PANEL_CLIENTES);
                break;
            case VentanaPrincipal.PANEL_CLIENTE:
                refrescarCatalogoCliente();
                ventanaPrincipal.cambiarPanel(VentanaPrincipal.PANEL_CLIENTE);
                break;

            default:
                System.err.println("Comando no reconocido: " + comando);
        }
    }

    // =========================== LOGIN / LOGOUT ===========================

    private void manejarLogin() {
        CredencialDto credencial = ventanaPrincipal.obtenerCredenciales();
        if (credencial == null
                || credencial.getCorreoElectronico() == null
                || credencial.getCorreoElectronico().trim().isEmpty()
                || credencial.getContrasena() == null
                || credencial.getContrasena().isEmpty()) {
            mostrarError("Ingrese el correo y la contraseña.");
            return;
        }

        if (!gestionSeguridad.iniciarSesion(credencial)) {
            mostrarError("Correo o contraseña incorrectos.");
            return;
        }

        ClienteDto usuario = ClienteMapper.toDto(gestionSeguridad.getClienteAutenticado());
        boolean esAdmin = usuario.getRol() == Rol.ADMIN;

        if (esAdmin) {
            refrescarTablaClientes(null);
            refrescarTablaLibros(null);
            ventanaPrincipal.cambiarPanel(VentanaPrincipal.PANEL_ADMIN);
        } else {
            ventanaPrincipal.getPanelCliente().setCliente(usuario);
            refrescarCatalogoCliente();
            ventanaPrincipal.cambiarPanel(VentanaPrincipal.PANEL_CLIENTE);
        }
    }

    private void manejarLogout() {
        gestionSeguridad.cerrarSesion();
        ventanaPrincipal.getPanelCliente().setCliente(null);
        ventanaPrincipal.getPanelLogin().limpiar();
        ventanaPrincipal.mostrarPanelLogin();
    }

    // =========================== CRUD CLIENTES ===========================

    /** CREATE de cliente: se usa desde el login y desde el panel del admin. */
    private void manejarNuevoCliente() {
        DialogoCliente dialogo = new DialogoCliente(ventanaPrincipal, null);
        dialogo.setVisible(true);
        if (!dialogo.isGuardado()) {
            return;
        }

        try {
            gestionCliente.registrarCliente(dialogo.getClienteDto());
            mostrarInformacion("Cliente registrado exitosamente.");
            refrescarTablaClientes(null);
        } catch (ReglaNegocioException ex) {
            mostrarError(ex.getMessage());
        } catch (Exception ex) {
            mostrarError("Error inesperado al registrar el cliente: " + ex.getMessage());
        }
    }

    /** UPDATE de cliente seleccionado en la tabla del administrador. */
    private void manejarEditarCliente() {
        ClienteDto seleccionado = ventanaPrincipal.getPanelGestionClientes().getClienteSeleccionado();
        if (seleccionado == null) {
            mostrarAdvertencia("Seleccione un cliente de la tabla.");
            return;
        }

        DialogoCliente dialogo = new DialogoCliente(ventanaPrincipal, seleccionado);
        dialogo.setVisible(true);
        if (!dialogo.isGuardado()) {
            return;
        }

        try {
            gestionCliente.actualizarCliente(dialogo.getClienteDto());
            mostrarInformacion("Cliente actualizado exitosamente.");
            refrescarTablaClientes(null);
            actualizarSesionActiva();
        } catch (ReglaNegocioException ex) {
            mostrarError(ex.getMessage());
        } catch (Exception ex) {
            mostrarError("Error inesperado al actualizar el cliente: " + ex.getMessage());
        }
    }

    /** DELETE de cliente seleccionado en la tabla del administrador. */
    private void manejarEliminarCliente() {
        ClienteDto seleccionado = ventanaPrincipal.getPanelGestionClientes().getClienteSeleccionado();
        if (seleccionado == null) {
            mostrarAdvertencia("Seleccione un cliente de la tabla.");
            return;
        }

        int opcion = JOptionPane.showConfirmDialog(
                ventanaPrincipal,
                "¿Desea eliminar al cliente \"" + seleccionado.getNombreCompleto() + "\"?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (opcion != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            gestionCliente.eliminarCliente(seleccionado.getIdCliente());
            mostrarInformacion("Cliente eliminado exitosamente.");
            refrescarTablaClientes(null);
        } catch (ReglaNegocioException ex) {
            mostrarError(ex.getMessage());
        } catch (Exception ex) {
            mostrarError("Error inesperado al eliminar el cliente: " + ex.getMessage());
        }
    }

    // =========================== CRUD LIBROS ===========================

    /** CREATE de libro. */
    private void manejarNuevoLibro() {
        DialogoLibro dialogo = new DialogoLibro(ventanaPrincipal, null, false);
        LibroDto libro = dialogo.mostrar();
        if (libro == null) {
            return;
        }

        try {
            gestionLibro.guardarLibro(libro);
            mostrarInformacion("Libro registrado exitosamente.");
            refrescarTablaLibros(null);
        } catch (ReglaNegocioException ex) {
            mostrarError(ex.getMessage());
        } catch (Exception ex) {
            mostrarError("Error inesperado al registrar el libro: " + ex.getMessage());
        }
    }

    /** UPDATE de libro seleccionado en la tabla del administrador. */
    private void manejarEditarLibro() {
        LibroDto seleccionado = ventanaPrincipal.getPanelGestionLibros().getLibroSeleccionado();
        if (seleccionado == null) {
            mostrarAdvertencia("Seleccione un libro de la tabla.");
            return;
        }

        DialogoLibro dialogo = new DialogoLibro(ventanaPrincipal, seleccionado, true);
        LibroDto libro = dialogo.mostrar();
        if (libro == null) {
            return;
        }

        try {
            gestionLibro.actualizarLibro(libro);
            mostrarInformacion("Libro actualizado exitosamente.");
            refrescarTablaLibros(null);
        } catch (ReglaNegocioException ex) {
            mostrarError(ex.getMessage());
        } catch (Exception ex) {
            mostrarError("Error inesperado al actualizar el libro: " + ex.getMessage());
        }
    }

    /** DELETE de libro seleccionado en la tabla del administrador. */
    private void manejarEliminarLibro() {
        LibroDto seleccionado = ventanaPrincipal.getPanelGestionLibros().getLibroSeleccionado();
        if (seleccionado == null) {
            mostrarAdvertencia("Seleccione un libro de la tabla.");
            return;
        }

        int opcion = JOptionPane.showConfirmDialog(
                ventanaPrincipal,
                "¿Desea eliminar el libro \"" + seleccionado.getTitulo() + "\"?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (opcion != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            gestionLibro.eliminarLibro(seleccionado.getIsbn());
            mostrarInformacion("Libro eliminado exitosamente.");
            refrescarTablaLibros(null);
        } catch (ReglaNegocioException ex) {
            mostrarError(ex.getMessage());
        } catch (Exception ex) {
            mostrarError("Error inesperado al eliminar el libro: " + ex.getMessage());
        }
    }

    // =========================== PERFIL DEL CLIENTE ===========================

    /** UPDATE de la propia cuenta del cliente autenticado. */
    private void manejarEditarPerfil() {
        ClienteDto cliente = ventanaPrincipal.getPanelCliente().getCliente();
        if (cliente == null) {
            mostrarAdvertencia("No hay un cliente autenticado.");
            return;
        }

        DialogoCliente dialogo = new DialogoCliente(ventanaPrincipal, cliente);
        dialogo.setVisible(true);
        if (!dialogo.isGuardado()) {
            return;
        }

        try {
            gestionCliente.actualizarCliente(dialogo.getClienteDto());
            ventanaPrincipal.getPanelCliente().setCliente(dialogo.getClienteDto());
            mostrarInformacion("Sus datos fueron actualizados exitosamente.");
        } catch (ReglaNegocioException ex) {
            mostrarError(ex.getMessage());
        } catch (Exception ex) {
            mostrarError("Error inesperado al actualizar sus datos: " + ex.getMessage());
        }
    }

    /** DELETE de la propia cuenta del cliente autenticado. */
    private void manejarEliminarCuenta() {
        ClienteDto cliente = ventanaPrincipal.getPanelCliente().getCliente();
        if (cliente == null) {
            mostrarAdvertencia("No hay un cliente autenticado.");
            return;
        }

        int opcion = JOptionPane.showConfirmDialog(
                ventanaPrincipal,
                "¿Realmente desea eliminar su cuenta? Esta acción no se puede deshacer.",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (opcion != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            gestionCliente.eliminarCliente(cliente.getIdCliente());
            mostrarInformacion("Su cuenta fue eliminada.");
            manejarLogout();
        } catch (ReglaNegocioException ex) {
            mostrarError(ex.getMessage());
        } catch (Exception ex) {
            mostrarError("Error inesperado al eliminar su cuenta: " + ex.getMessage());
        }
    }

    // =========================== REFRESCOS ===========================

    private void refrescarTablaClientes(String texto) {
        ventanaPrincipal.getPanelGestionClientes()
                .actualizarTabla(gestionCliente.buscarClientes(texto));
    }

    private void refrescarTablaLibros(FiltroLibroDto filtro) {
        ventanaPrincipal.getPanelGestionLibros().actualizarTabla(gestionLibro.listar(filtro));
    }

    private void refrescarCatalogoCliente() {
        String texto = ventanaPrincipal.getPanelCliente().getTextoBusqueda();
        ventanaPrincipal.getPanelCliente().setLibros(gestionLibro.listar(new FiltroLibroDto(texto, null, null)));
    }

    /** Si el usuario autenticado fue editado, actualiza el panel del cliente. */
    private void actualizarSesionActiva() {
        if (!gestionSeguridad.haySesionActiva()) {
            return;
        }
        int id = gestionSeguridad.getClienteAutenticado().getIdCliente();
        ClienteDto actualizado = gestionCliente.buscarPorId(id);
        if (actualizado == null) {
            manejarLogout();
            return;
        }
        ventanaPrincipal.getPanelCliente().setCliente(actualizado);
    }

    // =========================== MENSAJES ===========================

    private void mostrarInformacion(String mensaje) {
        JOptionPane.showMessageDialog(ventanaPrincipal, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarAdvertencia(String mensaje) {
        JOptionPane.showMessageDialog(ventanaPrincipal, mensaje, "Advertencia", JOptionPane.WARNING_MESSAGE);
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(ventanaPrincipal, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
