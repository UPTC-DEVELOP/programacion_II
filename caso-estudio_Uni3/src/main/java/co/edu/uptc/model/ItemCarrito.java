package co.edu.uptc.model;

// ItemCarrito permite modificar cantidades antes de confirmar la compra.
public class ItemCarrito extends LineaCompra {
    public ItemCarrito(String isbn, String titulo, double precioBase, double descuento,
                       double impuestoIVA, int cantidad) {
        super(isbn, titulo, precioBase, descuento, impuestoIVA, cantidad);
    }
    public void aumentar() { setCantidad(getCantidad() + 1); }
    public void disminuir() {
        if (getCantidad() > 1) setCantidad(getCantidad() - 1);
    }
}
