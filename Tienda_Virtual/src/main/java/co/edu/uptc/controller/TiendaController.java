package co.edu.uptc.controller;

import co.edu.uptc.model.*;
import co.edu.uptc.persistence.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class TiendaController {
    private final IRepositorioLibros repositorioLibros;
    private final IRepositorioUsuario repositorioUsuario;
    private final IRepositorioCarrito repositorioCarrito;
    private Cliente usuarioLogueado;

    public TiendaController() {
        this.repositorioLibros = new RepositorioLibrosJson();
        this.repositorioUsuario = new RepositorioUsuarioJson();
        this.repositorioCarrito = new RepositorioCarritoJson();
    }

    public boolean registrarCliente(Cliente cliente) {
        return repositorioUsuario.guardarUsuario(cliente);
    }

    public boolean iniciarSesion(String correo) {
        Cliente cliente = repositorioUsuario.buscarPorCorreo(correo);
        if (cliente != null) {
            this.usuarioLogueado = cliente;
            return true;
        }
        return false;
    }

    public Cliente getUsuarioLogueado() {
        return usuarioLogueado;
    }

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