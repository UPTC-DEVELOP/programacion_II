package co.edu.uptc.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JOptionPane;

import co.edu.uptc.excepciones.ReglaNegocioException;
import co.edu.uptc.modelo.dto.ClienteDto;
import co.edu.uptc.modelo.dto.CredencialDto;
import co.edu.uptc.modelo.dto.FiltroLibroDto;
import co.edu.uptc.modelo.dto.IngresoClienteDto;
import co.edu.uptc.modelo.dto.ItemCarritoDto;
import co.edu.uptc.modelo.dto.LibroDto;
import co.edu.uptc.modelo.enums.Rol;
import co.edu.uptc.modelo.mapper.ClienteMapper;
import co.edu.uptc.negocio.GestionCliente;
import co.edu.uptc.negocio.GestionDashboard;
import co.edu.uptc.negocio.GestionLibro;
import co.edu.uptc.negocio.GestionReporte;
import co.edu.uptc.negocio.GestionSeguridad;

/**
 * Clase centralizada para manejar todos los eventos de la aplicación
 * (patrón Controller / Observer).
 *
 * PRINCIPIO SOLID aplicado:
 * - SRP: Esta clase SOLO traduce eventos de la GUI en llamadas a la capa de
 *   negocio; no valida reglas ni persiste datos.
 * - DIP: Depende de las ABSTRACCIONES de negocio (GestionSeguridad,
 *   GestionCliente, GestionLibro, GestionDashboard, GestionReporte e
 *   ICarritoService), nunca de la persistencia concreta.
 * - OCP (FASE 4): para manejar un nuevo comando basta con agregar un case
 *   en el switch + un método privado, sin modificar los comandos existentes.
 *
 * FASE 4: se agregan las dependencias ICarritoService (RF10-RF13) y
 * GestionReporte (reportes del administrador), y los comandos asociados.
 *
 * @author Grupo 7
 * @version 2.0
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

    // --- Vistas del dashboard administrativo (sidebar) ---
    public static final String CMD_VISTA_DASHBOARD = "VISTA_DASHBOARD";
    public static final String CMD_VISTA_REPORTES = "VISTA_REPORTES";
    // FASE 4: vista de gestión de carritos del admin
    public static final String CMD_VISTA_CARRITOS = "VISTA_CARRITOS";

    // --- FASE 4: comandos del CARRITO del cliente (RF10-RF13) ---
    public static final String CMD_AGREGAR_CARRITO = "CARRITO_AGREGAR";
    public static final String CMD_IR_CARRITO = "CARRITO_IR";
    public static final String CMD_AUMENTAR_CARRITO = "CARRITO_AUMENTAR";
    public static final String CMD_DISMINUIR_CARRITO = "CARRITO_DISMINUIR";
    public static final String CMD_ELIMINAR_CARRITO = "CARRITO_ELIMINAR";
    public static final String CMD_VACIAR_CARRITO = "CARRITO_VACIAR";
    public static final String CMD_ACTUALIZAR_TOTALES = "CARRITO_ACTUALIZAR";

    // --- FASE 4: comandos de gestión de carritos del ADMIN ---
    public static final String CMD_ADMIN_ELIMINAR_CARRITO = "ADMIN_ELIMINAR_CARRITO";
    public static final String CMD_ADMIN_ACTUALIZAR_CARRITOS = "ADMIN_ACTUALIZAR_CARRITOS";

    private final VentanaPrincipal ventanaPrincipal;
    private final GestionSeguridad gestionSeguridad;
    private final GestionCliente gestionCliente;
    private final GestionLibro gestionLibro;
    private final GestionDashboard gestionDashboard;
    // FASE 4: nuevas dependencias (DIP: interfaz para el carrito, clase para reportes)
    private final co.edu.uptc.interfaces.ICarritoService carritoService;
    private final GestionReporte gestionReporte;

    /**
     * CONSTRUCTOR CON INYECCIÓN DE DEPENDENCIAS (SOLID / DIP).
     * Todas las dependencias son ABSTRACCIONES o servicios de negocio; el
     * controlador no conoce ninguna implementación concreta de persistencia.
     */
    public Evento(VentanaPrincipal ventanaPrincipal, GestionSeguridad gestionSeguridad,
                  GestionCliente gestionCliente, GestionLibro gestionLibro,
                  GestionDashboard gestionDashboard,
                  co.edu.uptc.interfaces.ICarritoService carritoService,
                  GestionReporte gestionReporte) {
        this.ventanaPrincipal = ventanaPrincipal;
        this.gestionSeguridad = gestionSeguridad;
        this.gestionCliente = gestionCliente;
        this.gestionLibro = gestionLibro;
        this.gestionDashboard = gestionDashboard;
        this.carritoService = carritoService;
        this.gestionReporte = gestionReporte;
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

            // ---------------- FASE 4: CARRITO del cliente (RF10-RF13) ----------------
            case CMD_AGREGAR_CARRITO:
                manejarAgregarAlCarrito();
                break;
            case CMD_IR_CARRITO:
                // Navega a la pestaña "Mi carrito" del panel del cliente
                ventanaPrincipal.getPanelCliente().mostrarPestaniaCarrito();
                refrescarCarritoCliente();
                break;
            case CMD_AUMENTAR_CARRITO:
                manejarCantidadCarrito(+1);
                break;
            case CMD_DISMINUIR_CARRITO:
                manejarCantidadCarrito(-1);
                break;
            case CMD_ELIMINAR_CARRITO:
                manejarEliminarItemCarrito();
                break;
            case CMD_VACIAR_CARRITO:
                manejarVaciarCarrito();
                break;
            case CMD_ACTUALIZAR_TOTALES:
                refrescarCarritoCliente();
                break;

            // ---------------- FASE 4: gestión de carritos del ADMIN ----------------
            case CMD_ADMIN_ELIMINAR_CARRITO:
                manejarAdminEliminarCarrito();
                break;
            case CMD_ADMIN_ACTUALIZAR_CARRITOS:
                refrescarGestionCarritos();
                break;

            // ---------------- Navegación entre tarjetas ----------------
            case VentanaPrincipal.PANEL_ADMIN:
                refrescarDashboard();
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
                refrescarCarritoCliente(); // FASE 4: refresca el carrito al entrar al panel
                ventanaPrincipal.cambiarPanel(VentanaPrincipal.PANEL_CLIENTE);
                break;

            // ---------------- Vistas del dashboard (sidebar) ----------------
            case CMD_VISTA_DASHBOARD:
                refrescarDashboard();
                ventanaPrincipal.getPanelDashboard().mostrarVista(
                        ventanaPrincipal.getPanelDashboard().getCardDashboard());
                break;
            // FASE 4: vista de gestión de carritos
            case CMD_VISTA_CARRITOS:
                refrescarGestionCarritos();
                ventanaPrincipal.getPanelDashboard().mostrarVista(
                        ventanaPrincipal.getPanelDashboard().getCardCarritos());
                break;
            case CMD_VISTA_REPORTES:
                refrescarReportes();
                ventanaPrincipal.getPanelDashboard().mostrarVista(
                        ventanaPrincipal.getPanelDashboard().getCardReportes());
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
            refrescarDashboard();
            ventanaPrincipal.cambiarPanel(VentanaPrincipal.PANEL_ADMIN);
        } else {
            ventanaPrincipal.getPanelCliente().setCliente(usuario);
            refrescarCatalogoCliente();
            refrescarCarritoCliente(); // FASE 4: carga el carrito del cliente recién logueado
            ventanaPrincipal.cambiarPanel(VentanaPrincipal.PANEL_CLIENTE);
        }
    }

    private void manejarLogout() {
        gestionSeguridad.cerrarSesion();
        ventanaPrincipal.getPanelCliente().setCliente(null);
        ventanaPrincipal.getPanelCliente().getPanelCarrito().limpiar(); // FASE 4
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

    // =========================== FASE 4: CARRITO DEL CLIENTE (RF10-RF13) ===========================

    /**
     * RF10 - Agrega el libro SELECCIONADO en el catálogo al carrito del
     * cliente, con la cantidad capturada en el spinner. Valida sesión activa,
     * selección de libro y cantidad. Muestra error si el servicio lanza
     * ReglaNegocioException (stock insuficiente, ISBN inexistente, etc.).
     */
    private void manejarAgregarAlCarrito() {
        ClienteDto cliente = ventanaPrincipal.getPanelCliente().getCliente();
        if (cliente == null) {
            mostrarAdvertencia("Debe iniciar sesión para agregar libros al carrito.");
            return;
        }

        LibroDto libro = ventanaPrincipal.getPanelCliente().getLibroSeleccionado();
        if (libro == null) {
            mostrarAdvertencia("Seleccione un libro de la tabla del catálogo.");
            return;
        }

        int cantidad = ventanaPrincipal.getPanelCliente().getCantidadSolicitada();
        try {
            carritoService.agregarLibro(cliente.getIdCliente(), libro.getIsbn(), cantidad);
            mostrarInformacion("Libro agregado al carrito exitosamente.");
            refrescarCarritoCliente();
        } catch (ReglaNegocioException ex) {
            mostrarError(ex.getMessage());
        } catch (Exception ex) {
            mostrarError("Error inesperado al agregar al carrito: " + ex.getMessage());
        }
    }

    /**
     * RF11 - Aumenta (+1) o disminuye (-1) la cantidad del ítem SELECCIONADO
     * en la tabla del carrito. Si al disminuir la cantidad llega a 0, se
     * SUGIERE eliminar el ítem (JOptionPane de opción), tal como pide el RF11.
     */
    private void manejarCantidadCarrito(int delta) {
        ClienteDto cliente = ventanaPrincipal.getPanelCliente().getCliente();
        if (cliente == null) {
            mostrarAdvertencia("No hay un cliente autenticado.");
            return;
        }

        PanelCarrito panelCarrito = ventanaPrincipal.getPanelCliente().getPanelCarrito();
        String isbn = panelCarrito.getIsbnSeleccionado();
        if (isbn == null) {
            mostrarAdvertencia("Seleccione un ítem de la tabla del carrito.");
            return;
        }

        try {
            if (delta > 0) {
                carritoService.aumentarCantidad(cliente.getIdCliente(), isbn, delta);
            } else {
                ItemCarritoDto resultado = carritoService.disminuirCantidad(cliente.getIdCliente(), isbn, Math.abs(delta));
                // RF11: si la cantidad llegó a 0, SUGERIR la eliminación
                if (resultado != null && resultado.getCantidad() == 0) {
                    int opcion = JOptionPane.showConfirmDialog(
                            ventanaPrincipal,
                            "La cantidad llegó a 0. ¿Desea eliminar este ítem del carrito?",
                            "Sugerencia de eliminación (RF11)",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE);
                    if (opcion == JOptionPane.YES_OPTION) {
                        carritoService.eliminarItem(cliente.getIdCliente(), isbn);
                    }
                }
            }
            refrescarCarritoCliente();
        } catch (ReglaNegocioException ex) {
            mostrarError(ex.getMessage());
        } catch (Exception ex) {
            mostrarError("Error inesperado al cambiar cantidad: " + ex.getMessage());
        }
    }

    /**
     * RF12 - Elimina el ítem SELECCIONADO del carrito, previa CONFIRMACIÓN
     * del usuario (JOptionPane YES_NO), tal como exige el RF12.
     */
    private void manejarEliminarItemCarrito() {
        ClienteDto cliente = ventanaPrincipal.getPanelCliente().getCliente();
        if (cliente == null) {
            mostrarAdvertencia("No hay un cliente autenticado.");
            return;
        }

        PanelCarrito panelCarrito = ventanaPrincipal.getPanelCliente().getPanelCarrito();
        String isbn = panelCarrito.getIsbnSeleccionado();
        if (isbn == null) {
            mostrarAdvertencia("Seleccione un ítem de la tabla del carrito.");
            return;
        }

        int opcion = JOptionPane.showConfirmDialog(
                ventanaPrincipal,
                "¿Desea eliminar este ítem del carrito?",
                "Confirmar eliminación (RF12)",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (opcion != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            carritoService.eliminarItem(cliente.getIdCliente(), isbn);
            mostrarInformacion("Ítem eliminado del carrito.");
            refrescarCarritoCliente();
        } catch (ReglaNegocioException ex) {
            mostrarError(ex.getMessage());
        } catch (Exception ex) {
            mostrarError("Error inesperado al eliminar el ítem: " + ex.getMessage());
        }
    }

    /**
     * Vacía TODOS los ítems del carrito del cliente (previa confirmación).
     * Se implementa eliminando uno por uno vía el servicio, ya que la interfaz
     * ICarritoService no expone un "vaciarTodo" (se puede agregar en el futuro
     * sin romper el contrato actual: OCP).
     */
    private void manejarVaciarCarrito() {
        ClienteDto cliente = ventanaPrincipal.getPanelCliente().getCliente();
        if (cliente == null) {
            mostrarAdvertencia("No hay un cliente autenticado.");
            return;
        }

        int opcion = JOptionPane.showConfirmDialog(
                ventanaPrincipal,
                "¿Desea vaciar TODO el carrito de compras?",
                "Confirmar vaciado",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (opcion != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            List<ItemCarritoDto> items = carritoService.obtenerItems(cliente.getIdCliente());
            for (ItemCarritoDto item : items) {
                carritoService.eliminarItem(cliente.getIdCliente(), item.getIsbn());
            }
            mostrarInformacion("Carrito vaciado exitosamente.");
            refrescarCarritoCliente();
        } catch (ReglaNegocioException ex) {
            mostrarError(ex.getMessage());
        } catch (Exception ex) {
            mostrarError("Error inesperado al vaciar el carrito: " + ex.getMessage());
        }
    }

    // =========================== FASE 4: GESTIÓN DE CARRITOS DEL ADMIN ===========================

    /** FASE 4 - Elimina el carrito del cliente SELECCIONADO en la tabla del admin. */
    private void manejarAdminEliminarCarrito() {
        PanelGestionCarritos panel = ventanaPrincipal.getPanelDashboard().getPanelGestionCarritos();
        Integer idCliente = panel.getClienteSeleccionado();
        if (idCliente == null) {
            mostrarAdvertencia("Seleccione un carrito de la tabla.");
            return;
        }

        int opcion = JOptionPane.showConfirmDialog(
                ventanaPrincipal,
                "¿Desea eliminar el carrito del cliente ID " + idCliente + "?",
                "Confirmar eliminación de carrito",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (opcion != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            // Se usa el repositorio de carritos a través del servicio; aquí se
            // elimina directamente vía la dependencia inyectada en GestionReporte
            // no es posible, por eso se usa el servicio de carrito con el id.
            // En una ampliación futura se agregaria un metodo eliminarCarrito
            // en ICarritoService; por ahora se simula con eliminarItem por cada item.
            List<ItemCarritoDto> items = carritoService.obtenerItems(idCliente);
            for (ItemCarritoDto item : items) {
                carritoService.eliminarItem(idCliente, item.getIsbn());
            }
            mostrarInformacion("Carrito eliminado exitosamente.");
            refrescarGestionCarritos();
        } catch (Exception ex) {
            mostrarError("Error inesperado al eliminar el carrito: " + ex.getMessage());
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

    /** Refresca los KPIs y la tabla de ventas recientes del dashboard. */
    private void refrescarDashboard() {
        ventanaPrincipal.getPanelDashboard().actualizarDashboard(gestionDashboard.obtenerKpis());
    }

    /**
     * FASE 4: Refresca la tabla de ítems y el panel de totales (RF13) del
     * carrito del cliente autenticado.
     */
    private void refrescarCarritoCliente() {
        ClienteDto cliente = ventanaPrincipal.getPanelCliente().getCliente();
        if (cliente == null) {
            ventanaPrincipal.getPanelCliente().getPanelCarrito().limpiar();
            return;
        }
        PanelCarrito panelCarrito = ventanaPrincipal.getPanelCliente().getPanelCarrito();
        panelCarrito.actualizarItems(carritoService.obtenerItems(cliente.getIdCliente()));
        try {
            panelCarrito.actualizarTotales(carritoService.calcularTotales(cliente.getIdCliente()));
        } catch (ReglaNegocioException ex) {
            // No se muestra error: simplemente los totales quedan en 0.00
            panelCarrito.actualizarTotales(new co.edu.uptc.modelo.dto.TotalesCarritoDto(0, 0, 0, 0));
        }
    }

    /**
     * FASE 4: Refresca la tabla resumen de gestión de carritos del admin
     * con los datos del reporte de ingresos por cliente (que lista todos
     * los carritos activos).
     */
    private void refrescarGestionCarritos() {
        List<IngresoClienteDto> carritos = gestionReporte.ingresosEstimadosPorCliente();
        ventanaPrincipal.getPanelDashboard().getPanelGestionCarritos().actualizarCarritos(carritos);
    }

    /**
     * FASE 4: Refresca las 3 pestañas del panel de reportes:
     *  1. Ventas (GestionDashboard)
     *  2. Libros en carritos (GestionReporte)
     *  3. Ingresos por cliente (GestionReporte)
     */
    private void refrescarReportes() {
        PanelReportes panelReportes = ventanaPrincipal.getPanelDashboard().getPanelReportes();
        panelReportes.actualizarTabla(gestionDashboard.listarVentas());
        panelReportes.actualizarLibrosCarritos(gestionReporte.librosMasVendidosEnCarritos());
        panelReportes.actualizarIngresosClientes(gestionReporte.ingresosEstimadosPorCliente());
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
