package co.edu.uptc.tienda.interfaz;

import co.edu.uptc.tienda.modelo.Cliente;

public interface ICalculosCarrito {

    double calcularSubtotal();

    double calcularImpuestos();

    double calcularTotal();

    double calcularTotal(double porcentajeDescuento);

    double calcularPorcentajeDescuento(Cliente cliente);
}
