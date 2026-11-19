package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.CarritoCompras;
import model.Cliente;
import model.Compra;
import model.ItemCompra;
import model.Libro;
import persistencia.IRepositorio;
import persistencia.PersistenciaException;
import service.IServicioCompras;
import service.IServicioLibros;
import service.ResultadoCompra;

public class ControladorCompras implements IServicioCompras {

    private final IRepositorio<Compra> repositorio;
    private final IServicioLibros servicioLibros;
    private final ControladorOperaciones operaciones;

    public ControladorCompras(IRepositorio<Compra> repositorio) {
        this(repositorio, null, null);
    }

    public ControladorCompras(IRepositorio<Compra> repositorio, IServicioLibros servicioLibros) {
        this(repositorio, servicioLibros, null);
    }

    public ControladorCompras(IRepositorio<Compra> repositorio,
            IServicioLibros servicioLibros, ControladorOperaciones operaciones) {
        this.repositorio = repositorio;
        this.servicioLibros = servicioLibros;
        this.operaciones = operaciones;
        this.repositorio.cargar();
    }

    @Override
    public boolean registrarCompra(Compra compra) {
        if (compra == null
                || compra.getCorreoCliente() == null
                || compra.getCorreoCliente().trim().isEmpty()
                || compra.getItems() == null
                || compra.getItems().isEmpty()
                || compra.getTotal() < 0) {
            return false;
        }
        try {
            repositorio.agregar(compra);
            repositorio.guardar();
            return true;
        } catch (PersistenciaException e) {
            compra.setId(0);
            revertir();
            return false;
        }
    }

    @Override
    public ResultadoCompra finalizarCompra(Cliente cliente, CarritoCompras carrito, String metodoPago) {
        if (cliente == null) {
            return ResultadoCompra.error("No hay una sesion iniciada.");
        }
        if (carrito == null || carrito.estaVacio()) {
            return ResultadoCompra.error("El carrito esta vacio.");
        }
        if (metodoPago == null || metodoPago.trim().isEmpty()) {
            return ResultadoCompra.error("Seleccione un metodo de pago.");
        }
        if (servicioLibros == null) {
            return ResultadoCompra.error("No esta configurado el servicio de libros.");
        }

        for (Map.Entry<Libro, Integer> entrada : carrito.getItems().entrySet()) {
            Libro libro = entrada.getKey();
            int cantidad = entrada.getValue();
            if (!servicioLibros.validarDisponibilidad(libro.getIsbn(), cantidad)) {
                return ResultadoCompra.error(
                        "No hay suficiente stock para: " + libro.getTitulo());
            }
        }

        Compra compra = new Compra(metodoPago.trim());
        compra.setCorreoCliente(cliente.getCorreo());

        for (Map.Entry<Libro, Integer> entrada : carrito.getItems().entrySet()) {
            compra.agregarItem(new ItemCompra(entrada.getKey(), entrada.getValue()));
        }

        compra.setTotal(carrito.calcularTotal(cliente.calcularDescuento()));

        if (!registrarCompra(compra)) {
            return ResultadoCompra.error(
                    "No se pudo guardar la compra. Verifique el almacenamiento.");
        }

        StringBuilder fallosInventario = new StringBuilder();
        for (Map.Entry<Libro, Integer> entrada : carrito.getItems().entrySet()) {
            Libro libro = entrada.getKey();
            int cantidad = entrada.getValue();
            if (!servicioLibros.actualizarInventario(libro.getIsbn(), cantidad)) {
                fallosInventario.append(libro.getTitulo()).append("\n");
            }
        }

        cliente.agregarCompra(compra);
        registrarOperacion("Compra registrada: " + compra.getId()
                + " de " + cliente.getCorreo()
                + " por " + String.format("$%,.2f", compra.getTotal()));

        String mensaje = "COMPRA REALIZADA CORRECTAMENTE\n\n"
                + "Cliente: " + cliente.getNombreCompleto()
                + "\nMetodo de pago: " + compra.getMetodoPago()
                + "\nFecha: " + compra.getFecha()
                + "\nTotal: " + String.format("$%,.2f", compra.getTotal());

        if (fallosInventario.length() > 0) {
            mensaje += "\n\nAviso: no se pudo actualizar el inventario de:\n"
                    + fallosInventario;
        }

        carrito.vaciar();
        return ResultadoCompra.exito(mensaje, compra);
    }

    @Override
    public List<Compra> listarCompras() {
        return new ArrayList<Compra>(repositorio.listar());
    }

    @Override
    public List<Compra> listarPorCliente(String correo) {
        List<Compra> resultado = new ArrayList<Compra>();
        if (correo == null || correo.trim().isEmpty()) {
            return resultado;
        }
        for (Compra compra : repositorio.listar()) {
            if (correo.equalsIgnoreCase(compra.getCorreoCliente())) {
                resultado.add(compra);
            }
        }
        return resultado;
    }

    @Override
    public int cantidad() {
        return repositorio.listar().size();
    }

    private void registrarOperacion(String descripcion) {
        if (operaciones != null) {
            operaciones.registrar(descripcion);
        }
    }

    private void revertir() {
        try {
            repositorio.cargar();
        } catch (PersistenciaException e) {
            System.err.println("No se pudo recargar el estado de compras: " + e.getMessage());
        }
    }
}
