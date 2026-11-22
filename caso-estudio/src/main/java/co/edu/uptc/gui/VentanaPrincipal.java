package co.edu.uptc.gui;

import javax.swing.*;
import java.util.List;
import java.util.stream.Collectors;
import co.edu.uptc.model.Cliente;
import co.edu.uptc.model.Libro;
import co.edu.uptc.negocio.*;
import co.edu.uptc.negocio.dto.CredencialDto;

public class VentanaPrincipal extends JFrame {
    private static final long serialVersionUID = 1L;

    private final IGestionUsuarios gestionUsuarios;
    private final IGestionLibros gestionLibros;
    private final IGestionCompras gestionCompras;

    private PanelUsuario panelUsuario;
    private PanelLibros panelLibros;
    private PanelCarrito panelCarrito;

    public VentanaPrincipal() {
        TiendaConfig config = new TiendaConfig();
        this.gestionUsuarios = config.getGestionUsuarios();
        this.gestionLibros = config.getGestionLibros();
        this.gestionCompras = config.getGestionCompras();
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
        if (c.getId().isEmpty() || c.getNombreCompleto().isEmpty() || c.getCorreo().isEmpty() || c.getContrasenaHash().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete los campos obligatorios.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (c.getContrasenaHash().length() < GestionUsuariosImpl.LONGITUD_MINIMA_CONTRASENA) {
            JOptionPane.showMessageDialog(this, "La contraseña debe tener mínimo " + GestionUsuariosImpl.LONGITUD_MINIMA_CONTRASENA + " caracteres.");
            return;
        }
        if (gestionUsuarios.registrarCliente(c)) {
            JOptionPane.showMessageDialog(this, "¡Usuario registrado exitosamente!");
        } else {
            JOptionPane.showMessageDialog(this, "El ID o el Correo ya están registrados.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void iniciarSesion() {
        CredencialDto cred = panelUsuario.obtenerCredenciales();
        if (cred.getCorreo().isEmpty() || cred.getContrasena().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese Correo y Contraseña.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (gestionUsuarios.iniciarSesion(cred)) {
            Cliente log = gestionUsuarios.getUsuarioLogueado();
            panelUsuario.cargarDatosUsuario(log);
            JOptionPane.showMessageDialog(this, "Bienvenido " + log.getNombreCompleto());
            refrescarCarrito();
        } else {
            JOptionPane.showMessageDialog(this, "Credenciales incorrectas.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void cerrarSesion() {
        gestionUsuarios.cerrarSesion();
        panelUsuario.limpiarFormulario();
        refrescarCarrito();
        JOptionPane.showMessageDialog(this, "Sesión cerrada.");
    }

    public void actualizarDatosUsuario() {
        Cliente logueado = gestionUsuarios.getUsuarioLogueado();
        if (logueado == null) {
            JOptionPane.showMessageDialog(this, "Debe iniciar sesión para actualizar sus datos.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Cliente datos = panelUsuario.construirClienteDesdeFormulario(logueado.getId(), logueado.getCorreo());
        String error = gestionUsuarios.actualizarUsuario(datos, panelUsuario.obtenerPasswordIngresada());
        if (error != null) {
            JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        panelUsuario.cargarDatosUsuario(gestionUsuarios.getUsuarioLogueado());
        JOptionPane.showMessageDialog(this, "Datos actualizados correctamente.");
    }

    public void eliminarCuentaUsuario() {
        if (gestionUsuarios.getUsuarioLogueado() == null) {
            JOptionPane.showMessageDialog(this, "Debe iniciar sesión para eliminar su cuenta.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        JPasswordField campoPass = new JPasswordField();
        int op = JOptionPane.showConfirmDialog(this,
            new Object[]{"Esta acción eliminará su cuenta de forma permanente.", "Ingrese su contraseña para confirmar:", campoPass},
            "Eliminar cuenta", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
        if (op != JOptionPane.OK_OPTION) return;

        if (gestionUsuarios.eliminarUsuarioLogueado(new String(campoPass.getPassword()))) {
            panelUsuario.limpiarFormulario();
            refrescarCarrito();
            refrescarLibros();
            JOptionPane.showMessageDialog(this, "Su cuenta fue eliminada correctamente.");
        } else {
            JOptionPane.showMessageDialog(this, "Contraseña incorrecta. No se eliminó la cuenta.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void mostrarOlvidoContrasena() {
        JOptionPane.showMessageDialog(this,
            "La recuperación de contraseña por correo no está disponible en esta versión.\nContacte al administrador de la tienda.");
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
        String q = panelLibros.getTextoBusqueda();
        if (q.isEmpty()) {
            refrescarLibros();
            return;
        }
        List<Libro> filtrados = gestionLibros.listarLibros().stream()
            .filter(l -> l.getIsbn().toLowerCase().contains(q) || l.getTitulo().toLowerCase().contains(q)
                      || l.getAutor().toLowerCase().contains(q) || l.getCategoria().toLowerCase().contains(q))
            .collect(Collectors.toList());
        panelLibros.actualizarTabla(filtrados);
    }

    public void abrirDialogoLibro(Libro existente) {
        DialogoLibro dialogo = new DialogoLibro(this, existente);
        dialogo.setVisible(true);
        Libro resultado = dialogo.getLibroResultado();
        if (resultado != null) {
            String error = (existente != null) ? gestionLibros.actualizarLibro(resultado) : gestionLibros.registrarLibro(resultado);
            if (error != null) {
                JOptionPane.showMessageDialog(this, error, "Error de Validación", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "¡Libro guardado con éxito!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                refrescarLibros();
            }
        }
    }

    public void editarLibroSeleccionado() {
        String isbn = panelLibros.getIsbnSeleccionado();
        if (isbn == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un libro para editar.");
            return;
        }
        Libro libro = gestionLibros.buscarLibroPorIsbn(isbn);
        if (libro != null) abrirDialogoLibro(libro);
    }

    public void eliminarLibroSeleccionado() {
        String isbn = panelLibros.getIsbnSeleccionado();
        if (isbn == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un libro para eliminar.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "¿Desea eliminar permanentemente el libro con ISBN " + isbn + "?",
            "Confirmar Eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            if (gestionLibros.eliminarLibro(isbn)) {
                JOptionPane.showMessageDialog(this, "Libro eliminado correctamente.");
                refrescarLibros();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo eliminar el libro.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void agregarLibroAlCarrito() {
        if (gestionUsuarios.getUsuarioLogueado() == null) {
            JOptionPane.showMessageDialog(this, "Debe iniciar sesión primero en la pestaña 'Usuario'.");
            return;
        }
        String isbn = panelLibros.getIsbnSeleccionado();
        if (isbn == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un libro del catálogo.");
            return;
        }
        Libro l = gestionLibros.buscarLibroPorIsbn(isbn);
        if (l != null) {
            String cantStr = JOptionPane.showInputDialog(this, "Ingrese la cantidad a comprar:", "1");
            if (cantStr != null) {
                try {
                    int cant = Integer.parseInt(cantStr);
                    if (cant <= 0) {
                        JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor a 0.");
                        return;
                    }
                    if (cant > l.getStock()) {
                        JOptionPane.showMessageDialog(this, "Stock insuficiente. Disponible: " + l.getStock());
                        return;
                    }
                    gestionCompras.agregarAlCarrito(l, cant);
                    refrescarCarrito();
                    refrescarLibros();
                    JOptionPane.showMessageDialog(this, "Libro añadido al carrito con éxito.");
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Cantidad inválida.");
                }
            }
        }
    }

    // ---------------- Acciones de Carrito ----------------
    
    public void refrescarCarrito() {
        panelCarrito.actualizarTabla(gestionCompras.obtenerCarritoActual().getItems());
    }

    public void actualizarCantidadCarrito() {
        if (gestionUsuarios.getUsuarioLogueado() == null) {
            JOptionPane.showMessageDialog(this, "Debe iniciar sesión primero en la pestaña 'Usuario'.");
            return;
        }
        String isbn = panelCarrito.getIsbnUnicoSeleccionado();
        if (isbn == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un solo libro del carrito para actualizar su cantidad.");
            return;
        }
        int cantActual = panelCarrito.getCantidadUnicaSeleccionada();
        String cantStr = JOptionPane.showInputDialog(this, "Ingrese la nueva cantidad:", String.valueOf(cantActual));
        if (cantStr != null) {
            try {
                int cant = Integer.parseInt(cantStr.trim());
                String error = gestionCompras.actualizarCantidadCarrito(isbn, cant);
                if (error != null) {
                    JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    refrescarCarrito();
                    refrescarLibros();
                    JOptionPane.showMessageDialog(this, "Cantidad actualizada correctamente.");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Cantidad inválida.");
            }
        }
    }

    public void eliminarDelCarrito() {
        if (gestionUsuarios.getUsuarioLogueado() == null) {
            JOptionPane.showMessageDialog(this, "Debe iniciar sesión primero en la pestaña 'Usuario'.");
            return;
        }
        List<String> isbns = panelCarrito.getIsbnsSeleccionados();
        if (isbns.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione al menos un libro del carrito para eliminar.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Desea eliminar " + isbns.size() + " libro(s) del carrito? Las unidades regresarán al stock.",
            "Confirmar Eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            if (gestionCompras.eliminarDelCarrito(isbns)) {
                JOptionPane.showMessageDialog(this, "Libro(s) eliminado(s) del carrito correctamente.");
                refrescarCarrito();
                refrescarLibros();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo eliminar del carrito.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void realizarCompra() {
        if (gestionUsuarios.getUsuarioLogueado() == null) {
            JOptionPane.showMessageDialog(this, "Debe iniciar sesión para realizar la compra.");
            return;
        }
        String errorPago = gestionCompras.registrarPago(panelCarrito.obtenerDatosPago());
        if (errorPago != null) {
            JOptionPane.showMessageDialog(this, errorPago, "Error en los datos de pago", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (gestionCompras.finalizarCompra()) {
            JOptionPane.showMessageDialog(this, "¡Compra realizada con éxito! Se ha procesado su orden.");
            refrescarCarrito();
            refrescarLibros();
        } else {
            JOptionPane.showMessageDialog(this, "El carrito está vacío o ocurrió un error al procesar.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaPrincipal().setVisible(true));
    }
}
