package co.edu.uptc.controller;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.List;

import co.edu.uptc.model.Carrito;
import co.edu.uptc.model.Cliente;
import co.edu.uptc.model.DatosTransferencia;
import co.edu.uptc.model.ItemCarrito;
import co.edu.uptc.model.Libro;
import co.edu.uptc.persistence.IRepositorioCarrito;
import co.edu.uptc.persistence.IRepositorioLibros;
import co.edu.uptc.persistence.IRepositorioUsuario;
import co.edu.uptc.persistence.RepositorioCarritoJson;
import co.edu.uptc.persistence.RepositorioLibrosJson;
import co.edu.uptc.persistence.RepositorioUsuarioJson;

public class TiendaController {

    private final IRepositorioLibros repositorioLibros;
    private final IRepositorioUsuario repositorioUsuario;
    private final IRepositorioCarrito repositorioCarrito;
    private Cliente usuarioLogueado;

    public static final int LONGITUD_MINIMA_CONTRASENA = 6;
    public static final String METODO_TRANSFERENCIA = "Transferencia";
    private DatosTransferencia datosTransferencia;
    public static final String METODO_TARJETA = "Tarjeta de Crédito";

    public TiendaController() {
        this.repositorioLibros = new RepositorioLibrosJson();
        this.repositorioUsuario = new RepositorioUsuarioJson();
        this.repositorioCarrito = new RepositorioCarritoJson();
    }

    // ---------------- Usuarios ----------------

    /**
     * Registra un nuevo usuario (operación CREAR del CRUD de Usuarios).
     * Valida que el ID y el correo no estén repetidos, cifra la contraseña
     * con SHA-256 y guarda el usuario en usuarios.json a través del repositorio.
     *
     * para cliente usuario a registrar; la vista entrega la contraseña en texto
     *                plano dentro de contrasenaHash y aquí se reemplaza por su hash
     * @return true si el usuario se guardó; false si los datos son inválidos
     *         o si el ID o el correo ya están registrados
     */
    public boolean registrarCliente(Cliente cliente) {
        // 1. Validación mínima: el objeto y sus datos clave no pueden ser nulos
        if (cliente == null || cliente.getId() == null || cliente.getCorreo() == null) return false;

        // 2. Regla de negocio: el ID debe ser único (no puede haber dos usuarios con el mismo ID)
        if (repositorioUsuario.buscarPorId(cliente.getId()) != null) return false;

        // 3. Regla de negocio: el correo debe ser único (se compara sin distinguir mayúsculas)
        if (repositorioUsuario.buscarPorCorreo(cliente.getCorreo()) != null) return false;

        // 4. Seguridad: nunca se guarda la contraseña en texto plano;
        //    se reemplaza por su hash SHA-256 antes de persistir
        cliente.setContrasenaHash(sha256(cliente.getContrasenaHash()));

        // 5. Persistencia: el repositorio (interfaz IRepositorioUsuario) escribe en usuarios.json
        return repositorioUsuario.guardarUsuario(cliente);
    }

    /**
     * Inicia sesión (operación CONSULTAR del CRUD de Usuarios).
     * Busca el usuario por correo y compara el hash de la contraseña ingresada
     * con el hash almacenado en usuarios.json.
     *
     * @return true si las credenciales son correctas y el usuario queda en sesión
     */
    public boolean iniciarSesion(String correo, String contrasena) {
        if (correo == null || contrasena == null) return false;
        Cliente c = repositorioUsuario.buscarPorCorreo(correo.trim());
        if (c != null && c.getContrasenaHash() != null && c.getContrasenaHash().equals(sha256(contrasena))) {
            this.usuarioLogueado = c;
            return true;
        }
        return false;
    }

    /** Cierra la sesión: el controlador deja de tener un usuario activo. */
    public void cerrarSesion() {
        this.usuarioLogueado = null;
    }

    public Cliente getUsuarioLogueado() {
        return usuarioLogueado;
    }

    /**
     * Actualiza los datos del usuario con sesión iniciada (operación ACTUALIZAR del CRUD).
     * El ID y el correo no se modifican porque identifican al usuario.
     *
     * para nuevosDatos     datos nuevos (nombre, dirección, departamento, ciudad, teléfono y tipo)
     * para nuevaContrasena nueva contraseña en texto plano; si es null o vacía se conserva la actual
     * @return null si se actualizó correctamente; en caso contrario, el mensaje de error
     */
    public String actualizarUsuario(Cliente nuevosDatos, String nuevaContrasena) {
        if (usuarioLogueado == null) return "Debe iniciar sesión para actualizar sus datos.";
        if (nuevosDatos == null) return "Los datos del usuario no pueden ser nulos.";
        if (nuevosDatos.getNombreCompleto() == null || nuevosDatos.getNombreCompleto().isBlank())
            return "El nombre no puede estar vacío.";

        boolean cambiaContrasena = nuevaContrasena != null && !nuevaContrasena.isEmpty();
        if (cambiaContrasena && nuevaContrasena.length() < LONGITUD_MINIMA_CONTRASENA)
            return "La contraseña debe tener mínimo " + LONGITUD_MINIMA_CONTRASENA + " caracteres.";

        String tipo = nuevosDatos.getTipoCliente() != null
                ? nuevosDatos.getTipoCliente() : usuarioLogueado.getTipoCliente();

        // Se conservan el ID y el correo; el resto de datos viene del formulario
        Cliente actualizado = new Cliente(
            usuarioLogueado.getId(), nuevosDatos.getNombreCompleto(), usuarioLogueado.getCorreo(),
            nuevosDatos.getDireccionEnvio(), nuevosDatos.getDepartamento(), nuevosDatos.getCiudad(),
            nuevosDatos.getTelefono(), tipo);

        // Si no se escribió una contraseña nueva, se conserva el hash actual
        actualizado.setContrasenaHash(cambiaContrasena ? sha256(nuevaContrasena) : usuarioLogueado.getContrasenaHash());

        // Persistencia: reemplaza el registro del usuario en usuarios.json
        if (!repositorioUsuario.actualizarUsuario(actualizado)) return "No se encontró el usuario para actualizar.";

        this.usuarioLogueado = actualizado;
        return null;
    }

    /**
     * Elimina la cuenta del usuario con sesión iniciada (operación ELIMINAR del CRUD).
     * Exige la contraseña actual como confirmación, devuelve al stock los libros de su carrito,
     * borra el carrito y cierra la sesión.
     *
     * @return true si la cuenta se eliminó; false si no hay sesión, la contraseña es incorrecta
     *         o no se pudo eliminar
     */
    public boolean eliminarUsuarioLogueado(String contrasena) {
        if (usuarioLogueado == null || contrasena == null) return false;

        // Confirmación: la contraseña ingresada debe coincidir con la almacenada
        if (!sha256(contrasena).equals(usuarioLogueado.getContrasenaHash())) return false;

        String id = usuarioLogueado.getId();
        if (!repositorioUsuario.eliminarUsuario(id)) return false;

        // El stock se descuenta al agregar al carrito: se devuelve antes de vaciar el carrito
        for (ItemCarrito item : obtenerCarritoActual().getItems()) {
            if (item.getLibro() != null) ajustarStock(item.getLibro().getIsbn(), item.getCantidad());
        }
        vaciarCarrito();                        // el carrito del usuario ya no tiene dueño
        this.usuarioLogueado = null;            // se cierra la sesión
        return true;
    }

    // ---------------- Libros ----------------

    public List<Libro> listarLibros() {
        return repositorioLibros.obtenerTodos();
    }

    public Libro buscarLibroPorIsbn(String isbn) {
        return repositorioLibros.buscarPorIsbn(isbn);
    }

    public String registrarLibro(Libro l) {
        String error = validarLibro(l, false);
        if (error != null) return error;

        boolean ok = repositorioLibros.guardarLibro(l);
        return ok ? null : "No se pudo guardar el libro en el repositorio.";
    }

    public String actualizarLibro(Libro l) {
        String error = validarLibro(l, true);
        if (error != null) return error;

        boolean ok = repositorioLibros.actualizarLibro(l);
        return ok ? null : "No se encontró el libro para actualizar.";
    }

    private String validarLibro(Libro l, boolean esEdicion) {
        if (l == null) return "El libro no puede ser nulo.";
        if (l.getIsbn() == null || l.getIsbn().isBlank()) return "El ISBN es obligatorio.";
        if (l.getTitulo() == null || l.getTitulo().isBlank()) return "El título no puede estar vacío.";
        if (l.getAutor() == null || l.getAutor().isBlank()) return "El autor no puede estar vacío.";
        if (l.getCategoria() == null || l.getCategoria().isBlank()) return "Debe seleccionar una categoría.";
        
        int anioActual = Calendar.getInstance().get(Calendar.YEAR);
        if (l.getAnio() < 1450 || l.getAnio() > anioActual) return "Año inválido (entre 1450 y " + anioActual + ").";
        if (l.getPaginas() <= 0) return "El número de páginas debe ser mayor a 0.";
        if (l.getPrecioVenta() <= 0) return "El precio de venta debe ser mayor a $0.";
        if (l.getStock() < 0) return "El stock no puede ser negativo.";
        if (l.getPorcentajeIva() < 0 || l.getPorcentajeIva() > 100) return "El IVA debe estar entre 0% y 100%.";

        if (!esEdicion && repositorioLibros.buscarPorIsbn(l.getIsbn()) != null) {
            return "Ya existe un libro con el ISBN: " + l.getIsbn();
        }
        return null;
    }

    public boolean eliminarLibro(String isbn) {
        return repositorioLibros.eliminarLibro(isbn);
    }

    // ---------------- Carrito & Compra ----------------

    public Carrito obtenerCarritoActual() {
        if (usuarioLogueado == null) return new Carrito("anonimo");
        return repositorioCarrito.obtenerCarritoPorCliente(usuarioLogueado.getId());
    }

    public void agregarAlCarrito(Libro libro, int cantidad) {
        if (usuarioLogueado == null) return;
        Carrito c = obtenerCarritoActual();
        c.agregarItem(libro, cantidad);
        repositorioCarrito.guardarCarrito(c);
        ajustarStock(libro.getIsbn(), -cantidad);
    }
    
    public String actualizarCantidadCarrito(String isbn, int nuevaCantidad) {
        if (usuarioLogueado == null) return "Debe iniciar sesión primero.";
        if (nuevaCantidad <= 0) return "La cantidad debe ser mayor a 0.";

        Carrito c = obtenerCarritoActual();
        ItemCarrito item = c.buscarItem(isbn);
        if (item == null) return "El libro no se encuentra en el carrito.";

        int diferencia = nuevaCantidad - item.getCantidad();
        Libro l = repositorioLibros.buscarPorIsbn(isbn);
        if (l != null && diferencia > l.getStock()) {
            return "Stock insuficiente. Disponible: " + l.getStock();
        }

        c.actualizarCantidad(isbn, nuevaCantidad);
        repositorioCarrito.guardarCarrito(c);
        ajustarStock(isbn, -diferencia);
        return null;
    }

    public boolean eliminarDelCarrito(List<String> isbns) {
        if (usuarioLogueado == null || isbns == null || isbns.isEmpty()) return false;

        Carrito c = obtenerCarritoActual();
        for (String isbn : isbns) {
            ItemCarrito item = c.buscarItem(isbn);
            if (item != null) {
                ajustarStock(isbn, item.getCantidad());
            }
        }

        boolean eliminado = c.eliminarItems(isbns);
        if (eliminado) {
            repositorioCarrito.guardarCarrito(c);
        }
        return eliminado;
    }

    private void ajustarStock(String isbn, int delta) {
        Libro l = repositorioLibros.buscarPorIsbn(isbn);
        if (l != null) {
            l.setStock(Math.max(l.getStock() + delta, 0));
            repositorioLibros.actualizarLibro(l);
        }
    }

    public void vaciarCarrito() {
        if (usuarioLogueado != null) {
            repositorioCarrito.vaciarCarrito(usuarioLogueado.getId());
        }
    }

    public String registrarPago(String metodo, String banco, String tarjeta, String exp, String cvv, String nequi) {
        if (METODO_TARJETA.equalsIgnoreCase(metodo)) {
            if (banco == null || banco.isBlank()) return "Seleccione un banco.";
            if (tarjeta == null || !tarjeta.matches("\\d{16}")) return "Número de tarjeta inválido (16 dígitos).";
            if (cvv == null || !cvv.matches("\\d{3,4}")) return "CVV inválido (3 o 4 dígitos).";
            if (exp == null || !exp.matches("(0[1-9]|1[0-2])/\\d{2}")) return "Fecha de expiración inválida (MM/AA).";
            this.datosTransferencia = new DatosTransferencia(banco, tarjeta, cvv, exp, null);
        } else if (METODO_TRANSFERENCIA.equalsIgnoreCase(metodo)) {
            if (nequi == null || !nequi.matches("3\\d{9}")) return "Número de Nequi inválido (10 dígitos empezando por 3).";
            this.datosTransferencia = new DatosTransferencia(DatosTransferencia.NEQUI, null, null, null, nequi);
        } else {
            return "Método de pago no válido.";
        }
        return null;
    }

    public boolean finalizarCompra() {
        if (usuarioLogueado == null) return false;
        Carrito c = obtenerCarritoActual();
        if (c.getItems().isEmpty()) return false;

        try (PrintWriter out = new PrintWriter(new FileWriter("operaciones_tienda.txt", true))) {
            out.println("COMPRA - Cliente: " + usuarioLogueado.getNombreCompleto() + " (" + usuarioLogueado.getCorreo() + ")");
            out.println("Total Pagado: $" + c.calcularTotal());
            out.println("----------------------------------------");
        } catch (IOException e) {
            e.printStackTrace();
        }

        vaciarCarrito();
        return true;
    }

    /**
     * Calcula el hash SHA-256 de un texto y lo devuelve en hexadecimal.
     * Se usa al registrar (para guardar la contraseña cifrada) y al iniciar
     * sesión (para compararla con la almacenada).
     */
    private String sha256(String base) {
        try {
            // Obtiene el algoritmo SHA-256 y calcula el hash de los bytes del texto (UTF-8)
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes(StandardCharsets.UTF_8));

            // Convierte cada byte a hexadecimal (2 caracteres, con 0 a la izquierda si hace falta)
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException ex) {
            // SHA-256 siempre está disponible en Java; si falla es un error del entorno
            throw new RuntimeException(ex);
        }
    }
}