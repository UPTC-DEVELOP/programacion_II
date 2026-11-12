import co.edu.uptc.model.*;
import java.util.ArrayList;

public class Phase1Smoke {
    static class ProductoPrueba extends Producto {
        ProductoPrueba(double precio, double descuento, double iva) {
            super("x", "x", precio, 10, descuento, iva);
        }
        public String getDescripcionEntrega() { return "prueba"; }
    }

    public static void main(String[] args) {
        Producto producto = new ProductoPrueba(80000, 10, 19);
        if (Math.round(producto.calcularPrecioFinal()) != 85680) {
            throw new AssertionError("precio final");
        }
        Cliente premium = new ClientePremium("Carlos", "carlos@correo.com", "hash",
                "Calle 1", "3001234567");
        if (Math.round(premium.calcularDescuento(119000)) != 11900) {
            throw new AssertionError("descuento premium");
        }
        Libro libro = LibroFactory.crear(FormatoLibro.FISICO, "978-958-00-0001-1",
                "Cien años de soledad", new ArrayList<String>(), 1967, Categoria.LITERATURA,
                "Editorial", 471, 50000, 10, 0, 19);
        if (Math.round(libro.calcularSubtotal(2)) != 100000
                || Math.round(libro.calcularImpuesto(2)) != 19000) {
            throw new AssertionError("totales libro");
        }
        System.out.println("PHASE1_MODEL_OK");
    }
}
