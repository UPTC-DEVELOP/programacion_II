package main.java.co.edu.uptc.persistence;
import main.java.co.edu.uptc.model.Carrito;

public interface IRepositorioCarrito {
    Carrito obtenerCarritoPorCliente(String clienteId);
    boolean guardarCarrito(Carrito carrito);
    boolean vaciarCarrito(String clienteId);
}