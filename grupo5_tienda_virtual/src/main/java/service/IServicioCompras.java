package service;

import java.util.List;

import model.Compra;

public interface IServicioCompras {

    boolean registrarCompra(Compra compra);

    List<Compra> listarCompras();

    List<Compra> listarPorCliente(String correo);

    int cantidad();
}
