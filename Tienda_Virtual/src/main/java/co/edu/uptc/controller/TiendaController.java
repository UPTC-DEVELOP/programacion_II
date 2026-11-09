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

    public boolean registrarCliente(Cliente cliente) {
        if (cliente == null || cliente.getId() == null || cliente.getCorreo() == null) return false;
        if (repositorioUsuario.buscarPorId(cliente.getId()) != null) return false;
        if (repositorioUsuario.buscarPorCorreo(cliente.getCorreo()) != null) return false;

        cliente.setContrasenaHash(sha256(cliente.getContrasenaHash()));
        return repositorioUsuario.guardarUsuario(cliente);
    }

    public boolean iniciarSesion(String correo, String contrasena) {
        if (correo == null || contrasena == null) return false;
        Cliente c = repositorioUsuario.buscarPorCorreo(correo.trim());
        if (c != null && c.getContrasenaHash() != null && c.getContrasenaHash().equals(sha256(contrasena))) {
            this.usuarioLogueado = c;
            return true;
        }
        return false;
    }

    public void cerrarSesion() {
        this.usuarioLogueado = null;
    }

    public Cliente getUsuarioLogueado() {
        return usuarioLogueado;
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

        // Validación de null para evitar NullPointerException si un libro se elimina del repositorio
        for (ItemCarrito item : c.getItems()) {
            if (item.getLibro() == null) continue;
            Libro l = repositorioLibros.buscarPorIsbn(item.getLibro().getIsbn());
            if (l != null) {
                int nuevoStock = l.getStock() - item.getCantidad();
                l.setStock(Math.max(nuevoStock, 0));
                repositorioLibros.actualizarLibro(l);
            }
        }

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

    private String sha256(String base) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
    }
}