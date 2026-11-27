package co.edu.uptc.caso.estudio.gui;

import co.edu.uptc.caso.estudio.model.Cliente;
import co.edu.uptc.caso.estudio.model.ItemCarrito;
import co.edu.uptc.caso.estudio.model.Libro;
import co.edu.uptc.caso.estudio.negocio.IGestionCompras;
import co.edu.uptc.caso.estudio.negocio.IGestionLibros;
import co.edu.uptc.caso.estudio.negocio.IGestionUsuarios;
import co.edu.uptc.caso.estudio.negocio.dto.CredencialDto;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

// VentanaPrincipal: integra los paneles visuales y se comunica con la capa de negocio.
public class VentanaPrincipal extends JFrame {

    private static final long serialVersionUID = 1L;

    private final IGestionLibros gestionLibros;
    private final IGestionUsuarios gestionUsuarios;
    private final IGestionCompras gestionCompras;

    private PanelUsuario panelUsuario;
    private PanelLibros panelLibros;
    private PanelCarrito panelCarrito;

    public VentanaPrincipal(IGestionLibros gestionLibros, IGestionUsuarios gestionUsuarios, IGestionCompras gestionCompras) {
        this.gestionLibros = gestionLibros;
        this.gestionUsuarios = gestionUsuarios;
        this.gestionCompras = gestionCompras;
        initUI();
    }

    private void initUI() {
        setTitle("Tienda Virtual de Libros - UPTC");
        setSize(1100, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        Evento evento = new Evento(this);
        panelUsuario = new PanelUsuario(evento);
        panelLibros = new PanelLibros(evento);
        panelCarrito = new PanelCarrito(evento);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Usuario", panelUsuario);
        tabbedPane.addTab("Catálogo & Inventario", panelLibros);
        tabbedPane.addTab("Carrito & Compra", panelCarrito);

        add(tabbedPane);
        refrescarLibros();
    }

    // ---------------- Acciones de Usuario ----------------

    public void registrarUsuario() {
        Cliente c = panelUsuario.construirClienteDesdeFormulario(null, null);
        String error = gestionUsuarios.registrarCliente(c);
        if (error != null) {
            mostrarMensaje(error, "Error de Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        panelUsuario.limpiarPassword();
        mostrarMensaje("¡Usuario registrado exitosamente!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    public void iniciarSesion() {
        CredencialDto cred = panelUsuario.obtenerCredenciales();
        if (cred.getCorreo().isEmpty() || cred.getContrasena().isEmpty()) {
            mostrarMensaje("Ingrese Correo y Contraseña.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (gestionUsuarios.iniciarSesion(cred)) {
            Cliente log = gestionUsuarios.getUsuarioLogueado();
            panelUsuario.cargarDatosUsuario(log);
            mostrarMensaje("Bienvenido " + log.getNombreCompleto(), "Sesión iniciada", JOptionPane.INFORMATION_MESSAGE);
            refrescarCarrito();
        } else {
            mostrarMensaje("Credenciales incorrectas.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void cerrarSesion() {
        gestionUsuarios.cerrarSesion();
        panelUsuario.limpiarFormulario();
        refrescarCarrito();
        mostrarMensaje("Sesión cerrada.", "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    public void actualizarDatosUsuario() {
        Cliente logueado = gestionUsuarios.getUsuarioLogueado();
        if (logueado == null) {
            mostrarMensaje("Debe iniciar sesión para actualizar sus datos.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Cliente datos = panelUsuario.construirClienteDesdeFormulario(logueado.getId(), logueado.getCorreo());
        String error = gestionUsuarios.actualizarUsuario(datos, panelUsuario.obtenerPasswordIngresada());
        if (error != null) {
            mostrarMensaje(error, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        panelUsuario.cargarDatosUsuario(gestionUsuarios.getUsuarioLogueado());
        refrescarCarrito();
        mostrarMensaje("Datos actualizados correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    public void eliminarCuentaUsuario() {
        if (gestionUsuarios.getUsuarioLogueado() == null) {
            mostrarMensaje("Debe iniciar sesión para eliminar su cuenta.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        JPasswordField campoPass = new JPasswordField();
        int op = JOptionPane.showConfirmDialog(
                this,
                new Object[]{"Esta acción eliminará su cuenta de forma permanente.", "Ingrese su contraseña para confirmar:", campoPass},
                "Eliminar cuenta",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE
        );
        if (op != JOptionPane.OK_OPTION) {
            return;
        }

        if (gestionUsuarios.eliminarUsuarioLogueado(new String(campoPass.getPassword()))) {
            panelUsuario.limpiarFormulario();
            refrescarCarrito();
            refrescarLibros();
            mostrarMensaje("Su cuenta fue eliminada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            mostrarMensaje("Contraseña incorrecta. No se eliminó la cuenta.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void mostrarOlvidoContrasena() {
        JTextField txtIdRec = new JTextField();
        JTextField txtCorreoRec = new JTextField();
        JPasswordField txtNuevaPass = new JPasswordField();

        Object[] formulario = {
            "Ingrese sus datos para verificar su identidad:",
            "ID / Cédula:", txtIdRec,
            "Correo Electrónico:", txtCorreoRec,
            "Nueva Contraseña (mín. " + IGestionUsuarios.LONGITUD_MINIMA_CONTRASENA + " caracteres):", txtNuevaPass
        };

        int opcion = JOptionPane.showConfirmDialog(
                this,
                formulario,
                "Restablecer Contraseña",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (opcion == JOptionPane.OK_OPTION) {
            String error = gestionUsuarios.restablecerContrasena(
                    txtIdRec.getText(), txtCorreoRec.getText(), new String(txtNuevaPass.getPassword()));
            if (error != null) {
                mostrarMensaje(error, "Error al restablecer", JOptionPane.ERROR_MESSAGE);
            } else {
                mostrarMensaje("¡Contraseña restablecida con éxito! Ya puede iniciar sesión.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    // ---------------- Acciones de Libros ----------------

    public void refrescarLibros() {
        panelLibros.actualizarTabla(gestionLibros.listarLibros());
    }

    public void mostrarTodosLosLibros() {
        panelLibros.limpiarBusqueda();
        refrescarLibros();
    }

    public void buscarLibros() {
        String q = panelLibros.getTextoBusqueda().toLowerCase();
        if (q.isEmpty()) {
            refrescarLibros();
            return;
        }

        List<Libro> filtrados = new ArrayList<>();
        List<Libro> todos = gestionLibros.listarLibros();
        for (Libro l : todos) {
            if (l != null && coincideBusqueda(l, q)) {
                filtrados.add(l);
            }
        }
        panelLibros.actualizarTabla(filtrados);
    }

    private boolean coincideBusqueda(Libro l, String consulta) {
        return contieneTexto(l.getIsbn(), consulta)
                || contieneTexto(l.getTitulo(), consulta)
                || contieneTexto(l.getAutor(), consulta)
                || contieneTexto(l.getCategoria(), consulta);
    }

    private boolean contieneTexto(String campo, String consulta) {
        return campo != null && campo.toLowerCase().contains(consulta);
    }

    public void abrirDialogoLibro(Libro existente) {
        DialogoLibro dialogo = new DialogoLibro(this, existente);
        dialogo.setVisible(true);
        Libro resultado = dialogo.getLibroResultado();
        if (resultado == null) {
            return;
        }

        String error = (existente != null)
                ? gestionLibros.actualizarLibro(resultado)
                : gestionLibros.registrarLibro(resultado);

        if (error != null) {
            mostrarMensaje(error, "Error de Validación", JOptionPane.ERROR_MESSAGE);
        } else {
            mostrarMensaje("¡Libro guardado con éxito!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            refrescarLibros();
            refrescarCarrito();
        }
    }

    public void editarLibroSeleccionado() {
        String isbn = panelLibros.getIsbnSeleccionado();
        if (isbn == null) {
            mostrarMensaje("Seleccione un libro para editar.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        Libro libro = gestionLibros.buscarLibroPorIsbn(isbn);
        if (libro != null) {
            abrirDialogoLibro(libro);
        }
    }

    public void eliminarLibroSeleccionado() {
        String isbn = panelLibros.getIsbnSeleccionado();
        if (isbn == null) {
            mostrarMensaje("Seleccione un libro para eliminar.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "¿Desea eliminar permanentemente el libro con ISBN " + isbn + "?",
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );
        if (confirm == JOptionPane.YES_OPTION) {
            if (gestionLibros.eliminarLibro(isbn)) {
                mostrarMensaje("Libro eliminado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                refrescarLibros();
                refrescarCarrito();
            } else {
                mostrarMensaje("No se pudo eliminar el libro.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void agregarLibroAlCarrito() {
        if (!haySesionActiva("Debe iniciar sesión primero en la pestaña 'Usuario'.")) {
            return;
        }
        String isbn = panelLibros.getIsbnSeleccionado();
        if (isbn == null) {
            mostrarMensaje("Seleccione un libro del catálogo.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        Libro l = gestionLibros.buscarLibroPorIsbn(isbn);
        if (l == null) {
            return;
        }

        String cantStr = JOptionPane.showInputDialog(this, "Ingrese la cantidad a comprar:", "1");
        if (cantStr == null) {
            return;
        }
        try {
            int cant = Integer.parseInt(cantStr.trim());
            if (cant <= 0) {
                mostrarMensaje("La cantidad debe ser mayor a 0.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            ItemCarrito existente = gestionCompras.obtenerCarritoActual().buscarItem(isbn);
            int enCarrito = (existente != null) ? existente.getCantidad() : 0;
            if (enCarrito + cant > l.getStock()) {
                mostrarMensaje(
                        "Stock insuficiente. Disponible: " + l.getStock() + " (en carrito: " + enCarrito + ").",
                        "Stock insuficiente",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }
            gestionCompras.agregarAlCarrito(l, cant);
            refrescarCarrito();
            mostrarMensaje("Libro añadido al carrito con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException e) {
            mostrarMensaje("Cantidad inválida.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ---------------- Acciones de Carrito ----------------

    public void refrescarCarrito() {
        panelCarrito.actualizarTabla(gestionCompras.obtenerCarritoActual().getItems(), gestionCompras.calcularTotalFinal());
    }

    public void actualizarCantidadCarrito() {
        if (!haySesionActiva("Debe iniciar sesión primero en la pestaña 'Usuario'.")) {
            return;
        }
        String isbn = panelCarrito.getIsbnUnicoSeleccionado();
        if (isbn == null) {
            mostrarMensaje("Seleccione un solo libro del carrito para actualizar su cantidad.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        int cantActual = panelCarrito.getCantidadUnicaSeleccionada();
        String cantStr = JOptionPane.showInputDialog(this, "Ingrese la nueva cantidad:", String.valueOf(cantActual));
        if (cantStr == null) {
            return;
        }
        try {
            int cant = Integer.parseInt(cantStr.trim());
            String error = gestionCompras.actualizarCantidadCarrito(isbn, cant);
            if (error != null) {
                mostrarMensaje(error, "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                refrescarCarrito();
                mostrarMensaje("Cantidad actualizada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (NumberFormatException e) {
            mostrarMensaje("Cantidad inválida.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void eliminarDelCarrito() {
        if (!haySesionActiva("Debe iniciar sesión primero en la pestaña 'Usuario'.")) {
            return;
        }
        List<String> isbns = panelCarrito.getIsbnsSeleccionados();
        if (isbns.isEmpty()) {
            mostrarMensaje("Seleccione al menos un libro del carrito para eliminar.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "¿Desea eliminar " + isbns.size() + " libro(s) del carrito?",
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );
        if (confirm == JOptionPane.YES_OPTION) {
            if (gestionCompras.eliminarDelCarrito(isbns)) {
                mostrarMensaje("Libro(s) eliminado(s) del carrito correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                refrescarCarrito();
            } else {
                mostrarMensaje("No se pudo eliminar del carrito.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void realizarCompra() {
        if (!haySesionActiva("Debe iniciar sesión para realizar la compra.")) {
            return;
        }
        String errorPago = gestionCompras.registrarPago(panelCarrito.obtenerDatosPago());
        if (errorPago != null) {
            mostrarMensaje(errorPago, "Error en los datos de pago", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (gestionCompras.finalizarCompra()) {
            panelCarrito.limpiarFormularioPago();
            mostrarMensaje("¡Gracias por tu compra! Se ha procesado tu orden con éxito.", "Compra Exitosa", JOptionPane.INFORMATION_MESSAGE);
            refrescarCarrito();
            refrescarLibros();
        } else {
            mostrarMensaje("El carrito está vacío o ocurrió un error al procesar la compra.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean haySesionActiva(String mensajeAviso) {
        if (gestionUsuarios.getUsuarioLogueado() == null) {
            mostrarMensaje(mensajeAviso, "Aviso", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private void mostrarMensaje(String mensaje, String titulo, int tipo) {
        JOptionPane.showMessageDialog(this, mensaje, titulo, tipo);
    }
}