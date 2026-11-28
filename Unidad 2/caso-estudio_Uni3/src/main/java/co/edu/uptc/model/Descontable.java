package co.edu.uptc.model;

// Permite que CompraService use cualquier tipo de cliente sin preguntar su clase.
public interface Descontable {
    double calcularDescuento(double totalCompra);
}
