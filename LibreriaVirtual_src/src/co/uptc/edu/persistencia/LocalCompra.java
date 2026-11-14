package co.uptc.edu.persistencia;

import java.util.List;

import co.uptc.edu.interfaces.IGestionCompra;
import co.uptc.edu.model.ItemCarrito;
import co.uptc.edu.model.Usuario;

/** Sin base de datos no se conserva historial: la compra solo descuenta el stock en memoria. */
public class LocalCompra implements IGestionCompra {

    @Override
    public void registrar(Usuario comprador, List<ItemCarrito> items, double subtotal, double iva, double total) {
        // No hay dónde guardar el historial.
    }
}
