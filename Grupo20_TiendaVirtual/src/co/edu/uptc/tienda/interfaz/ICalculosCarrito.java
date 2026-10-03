package co.edu.uptc.tienda.interfaz;

public interface ICalculosCarrito {

    double calcularSubtotal();

    double calcularImpuestos();

    double calcularTotal();

    double calcularTotal(double porcentajeDescuento);
}
