package service;

import java.util.List;

import model.CarritoCompras;
import model.Cliente;
import model.Compra;

public interface IServicioCompras {

    boolean registrarCompra(Compra compra);

    ResultadoCompra finalizarCompra(Cliente cliente, CarritoCompras carrito, String metodoPago);

    List<Compra> listarCompras();

    List<Compra> listarPorCliente(String correo);

    int cantidad();
}
