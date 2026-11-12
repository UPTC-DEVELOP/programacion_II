package service;

import java.util.List;

import model.Compra;

/**
 * Contrato de la logica de negocio de compras.
 *
 * Las ventanas dependen de esta interfaz y no del controlador concreto: si
 * cambia la forma de guardar las compras (JSON o base de datos), la interfaz
 * grafica no se entera (SOLID: Dependency Inversion).
 */
public interface IServicioCompras {

    
    boolean registrarCompra(Compra compra);

    List<Compra> listarCompras();
}
