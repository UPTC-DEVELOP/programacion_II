package co.edu.uptc.service;

import co.edu.uptc.model.Cliente;

// Desde el cuarto pedido se aplica un cinco por ciento adicional sobre el total bruto.
public class DescuentoClienteFrecuente implements PoliticaDescuentoAdicional {
    private static final double PORCENTAJE = 5.0;
    @Override public double calcularDescuento(Cliente cliente, double totalBruto, int pedidosAnteriores) {
        return pedidosAnteriores >= 3 ? totalBruto * PORCENTAJE / 100.0 : 0.0;
    }
}
