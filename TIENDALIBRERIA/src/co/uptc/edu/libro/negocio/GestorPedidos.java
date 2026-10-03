package co.uptc.edu.libro.negocio;

import java.util.ArrayList;

import co.uptc.edu.libro.modelo.Libro;

public class GestorPedidos {

    private ArrayList<Pedido> pedidos;

    public GestorPedidos() {
        pedidos = new ArrayList<Pedido>();
    }

    public void registrarPedido(Libro libro, int cantidad, double total) {

        Pedido pedido = new Pedido(libro, cantidad, total);

        pedidos.add(pedido);
    }

    public ArrayList<Pedido> getPedidos() {
        return pedidos;
    }

    public double calcularTotal(Libro libro, int cantidad) {

        return libro.getPrecioVenta() * cantidad;
    }
}