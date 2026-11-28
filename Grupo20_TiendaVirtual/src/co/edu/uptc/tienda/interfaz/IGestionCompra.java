package co.edu.uptc.tienda.interfaz;

import java.util.List;

import co.edu.uptc.tienda.excepcion.CompraExcepcion;
import co.edu.uptc.tienda.modelo.Cliente;
import co.edu.uptc.tienda.modelo.Compra;
import co.edu.uptc.tienda.modelo.ItemCarrito;

public interface IGestionCompra {

    Compra registrarCompra(Cliente cliente, String metodoPago, double porcentajeDescuento, List<ItemCarrito> items)
            throws CompraExcepcion;

    String getAvisoGuardado();

    List<Compra> listarCompras() throws CompraExcepcion;

    List<Compra> listarComprasCliente(String idCliente) throws CompraExcepcion;

    boolean tieneVentas(String isbn) throws CompraExcepcion;

    boolean clienteComproLibro(String idCliente, String isbn) throws CompraExcepcion;

    int contarCompras(String idCliente) throws CompraExcepcion;

    double obtenerDescuentoFrecuente(String idCliente);

    String generarRecibo(Compra compra);
}
