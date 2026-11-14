package co.edu.uptc.service;

import co.edu.uptc.model.Cliente;

// Una política adicional puede cambiarse o combinarse sin modificar CompraService.
public interface PoliticaDescuentoAdicional {
    double calcularDescuento(Cliente cliente, double totalBruto, int pedidosAnteriores);
}
