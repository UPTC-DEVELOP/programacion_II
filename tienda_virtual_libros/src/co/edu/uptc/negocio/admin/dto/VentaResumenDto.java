package co.edu.uptc.negocio.admin.dto;

import java.util.List;

/**
 * DTO VentaResumenDto  (paquete: negocio.dto)
 * ---------------------------------------------------------------------------
 * Resumen de UNA venta, solo lectura. Lo necesitan:
 *  - RF03 (saber si un libro tiene ventas asociadas),
 *  - el Dashboard ("Ventas recientes") y
 *  - la pantalla de Reportes.
 * Las ventas las crea el módulo del carrito/compra (otro compañero); este
 * módulo solo las CONSULTA, por eso el DTO es de solo lectura (sin setters).
 */
public class VentaResumenDto {

    private final String fecha;          // formato yyyy-MM-dd
    private final List<String> isbns;    // libros incluidos en la venta
    private final double total;

    public VentaResumenDto(String fecha, List<String> isbns, double total) {
        this.fecha = fecha;
        this.isbns = isbns;
        this.total = total;
    }

    public String getFecha() { return fecha; }
    public List<String> getIsbns() { return isbns; }
    public double getTotal() { return total; }
}
