package co.edu.uptc.negocio;

/**
 * DESCUENTO REGULAR: 0% (el cliente regular no tiene descuentos especiales).
 * Es la estrategia POR DEFECTO de la fábrica CalculadoraDescuento.porTipoCliente().
 */
public class DescuentoRegular extends CalculadoraDescuento {

    @Override
    public double calcularDescuento(double subtotal) {
        // Regla de negocio: los clientes REGULAR no obtienen descuento.
        return 0.0;
    }
}
