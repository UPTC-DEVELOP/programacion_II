package main.java.co.edu.uptc.controller;

import main.java.co.edu.uptc.model.*;
import main.java.co.edu.uptc.persistence.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class TiendaController {
    private final IRepositorioLibros repositorioLibros;
    private final IRepositorioUsuario repositorioUsuario;
    private final IRepositorioCarrito repositorioCarrito;
    private Cliente usuarioLogueado;
    public static final int LONGITUD_MINIMA_CONTRASENA = 6;

    public TiendaController() {
        this.repositorioLibros = new RepositorioLibrosJson();
        this.repositorioUsuario = new RepositorioUsuarioJson();
        this.repositorioCarrito = new RepositorioCarritoJson();
    }

    // ---------- Usuarios ----------

    public boolean registrarCliente(Cliente cliente) {
        // No se permiten IDs ni correos repetidos
        if (repositorioUsuario.buscarPorId(cliente.getId()) != null) return false;
        if (repositorioUsuario.buscarPorCorreo(cliente.getCorreo()) != null) return false;
        return repositorioUsuario.guardarUsuario(cliente);
    }

    public boolean registrarCliente(Cliente cliente, String contrasena) {
        if (contrasena == null || contrasena.length() < LONGITUD_MINIMA_CONTRASENA) return false;
        cliente.setContrasenaHash(hash(contrasena));
        return registrarCliente(cliente);
    }

    /** @deprecated Inicia sesión sin contraseña; usar {@link #iniciarSesion(String, String)}. */
    @Deprecated
    public boolean iniciarSesion(String correo) {
        Cliente cliente = repositorioUsuario.buscarPorCorreo(correo);
        if (cliente != null) {
            this.usuarioLogueado = cliente;
            return true;
        }
        return false;
    }

    public boolean iniciarSesion(String correo, String contrasena) {
        Cliente cliente = repositorioUsuario.buscarPorCorreo(correo);
        if (cliente == null || cliente.getContrasenaHash() == null || contrasena == null) return false;
        if (!cliente.getContrasenaHash().equals(hash(contrasena))) return false;
        this.usuarioLogueado = cliente;
        return true;
    }

    public void cerrarSesion() {
        this.usuarioLogueado = null;
    }

    /**
     * Actualiza los datos del usuario con sesión iniciada. El ID, la contraseña y el tipo
     * de cliente (con su descuento) se conservan o se toman del objeto recibido.
     */
    public boolean actualizarCliente(Cliente datos) throws Exception {
        if (usuarioLogueado == null) throw new Exception("Debe iniciar sesión primero.");

        Cliente otro = repositorioUsuario.buscarPorCorreo(datos.getCorreo());
        if (otro != null && !otro.getId().equals(usuarioLogueado.getId())) {
            throw new Exception("Ese correo ya está registrado por otro usuario.");
        }

        datos.setId(usuarioLogueado.getId());
        datos.setContrasenaHash(usuarioLogueado.getContrasenaHash());
        if (repositorioUsuario.actualizarUsuario(datos)) {
            this.usuarioLogueado = datos;
            return true;
        }
        return false;
    }

    /**
     * Recuperación de contraseña: se verifica identidad con correo + ID/cédula registrados
     * y se establece la nueva contraseña.
     */
    public boolean restablecerContrasena(String correo, String id, String nuevaContrasena) {
        if (nuevaContrasena == null || nuevaContrasena.length() < LONGITUD_MINIMA_CONTRASENA) return false;
        Cliente cliente = repositorioUsuario.buscarPorCorreo(correo);
        if (cliente == null || !cliente.getId().equals(id)) return false;
        cliente.setContrasenaHash(hash(nuevaContrasena));
        return repositorioUsuario.actualizarUsuario(cliente);
    }

    public Cliente getUsuarioLogueado() {
        return usuarioLogueado;
    }

    private static String hash(String texto) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(texto.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    // ---------- Libros y compra ----------

    public boolean registrarLibro(Libro libro) {
        return repositorioLibros.guardarLibro(libro);
    }

    public boolean actualizarLibro(Libro libro) {
        return repositorioLibros.actualizarLibro(libro);
    }

    public boolean eliminarLibro(String isbn) {
        return repositorioLibros.eliminarLibro(isbn);
    }

    public List<Libro> listarLibros() {
        return repositorioLibros.obtenerTodos();
    }

    public boolean agregarAlCarrito(String isbn, int cantidad) throws Exception {
        if (usuarioLogueado == null) throw new Exception("Debe iniciar sesión primero.");
        
        Libro libro = repositorioLibros.buscarPorIsbn(isbn);
        if (libro == null) throw new Exception("Libro no encontrado.");
        if (libro.getStock() < cantidad) throw new Exception("Stock insuficiente.");

        Carrito carrito = repositorioCarrito.obtenerCarritoPorCliente(usuarioLogueado.getId());
        
        boolean existe = false;
        for (ItemCarrito item : carrito.getItems()) {
            if (item.getLibro().getIsbn().equals(isbn)) {
                if (libro.getStock() < (item.getCantidad() + cantidad)) {
                    throw new Exception("Excede la cantidad disponible en inventario.");
                }
                item.setCantidad(item.getCantidad() + cantidad);
                existe = true;
                break;
            }
        }

        if (!existe) {
            carrito.getItems().add(new ItemCarrito(libro, cantidad));
        }

        return repositorioCarrito.guardarCarrito(carrito);
    }

    public Carrito obtenerCarritoActual() {
        if (usuarioLogueado == null) return new Carrito();
        return repositorioCarrito.obtenerCarritoPorCliente(usuarioLogueado.getId());
    }

    public double calcularTotalImpuestos(Carrito carrito) {
        return carrito.getItems().stream().mapToDouble(ItemCarrito::getImpuestos).sum();
    }

    public double calcularTotalCompra(Carrito carrito) {
        double subtotalConIva = carrito.getItems().stream().mapToDouble(ItemCarrito::getSubtotalConIva).sum();
        if (usuarioLogueado != null && usuarioLogueado.getTipoCliente().equalsIgnoreCase("PREMIUM")) {
            subtotalConIva -= (subtotalConIva * usuarioLogueado.getPorcentajeDescuento());
        }
        return subtotalConIva;
    }

    public boolean finalizarCompra(String metodoPago) throws Exception {
        if (usuarioLogueado == null) throw new Exception("Debe iniciar sesión.");
        
        Carrito carrito = obtenerCarritoActual();
        if (carrito.getItems().isEmpty()) throw new Exception("El carrito está vacío.");

        for (ItemCarrito item : carrito.getItems()) {
            Libro libro = repositorioLibros.buscarPorIsbn(item.getLibro().getIsbn());
            if (libro.getStock() < item.getCantidad()) {
                throw new Exception("Stock insuficiente para: " + libro.getTitulo());
            }
            libro.setStock(libro.getStock() - item.getCantidad());
            repositorioLibros.actualizarLibro(libro);
        }

        registrarLogTransaccion(carrito, metodoPago);
        repositorioCarrito.vaciarCarrito(usuarioLogueado.getId());
        return true;
    }

    private void registrarLogTransaccion(Carrito carrito, String metodoPago) {
        try (PrintWriter pw = new PrintWriter(new FileWriter("registro_operaciones.txt", true))) {
            pw.println("=== RECIBO DE COMPRA ===");
            pw.println("Cliente: " + usuarioLogueado.getNombreCompleto() + " (" + usuarioLogueado.getTipoCliente() + ")");
            pw.println("Método de Pago: " + metodoPago);
            for (ItemCarrito item : carrito.getItems()) {
                pw.println(" - " + item.getLibro().getTitulo() + " x" + item.getCantidad() + " = $" + item.getSubtotalConIva());
            }
            pw.println("Total Impuestos: $" + calcularTotalImpuestos(carrito));
            pw.println("TOTAL PAGADO: $" + calcularTotalCompra(carrito));
            pw.println("----------------------------------------\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}