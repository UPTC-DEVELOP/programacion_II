package co.uptc.edu.interfaces;

import java.util.List;

import co.uptc.edu.model.ItemCarrito;
import co.uptc.edu.model.Usuario;

//Contrato para dejar registro de las compras finalizadas (historial)
public interface IGestionCompra {

    //Registra una compra con sus libros 
    void registrar(Usuario comprador, List<ItemCarrito> items, double subtotal, double iva, double total);
}
