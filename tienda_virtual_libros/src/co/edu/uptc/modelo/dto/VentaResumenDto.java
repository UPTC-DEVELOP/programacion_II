package co.edu.uptc.modelo.dto;

import java.util.List;

/**
 * DTO VentaResumenDto  (paquete: modelo.dto)
 * ---------------------------------------------------------------------------
 * Resumen de UNA venta, solo lectura. Lo necesitan:
 *  - RF03 (saber si un libro tiene ventas asociadas),
 *  - el Dashboard ("Ventas recientes": ID, Fecha, Cliente, Total, Estado) y
 *  - la pantalla de Reportes.
 *
 * ¿POR QUÉ SE AGREGARON idVenta, nombreCliente y estado?
 *  - La tabla "Ventas Recientes" del dashboard exige mostrar EXACTAMENTE
 *    esas columnas: ID, Fecha, Cliente, Total y Estado. Sin esos campos en
 *    el DTO, la capa de presentación no tendría de dónde leerlos.
 *  - Se mantiene la lista de ISBNs por si Reportes necesita detalle de qué
 *    libros incluyó cada venta.
 *
 * Las ventas las crea el módulo del carrito/compra; este módulo solo las
 * CONSULTA, por eso el DTO es de solo lectura (sin setters).
 */
public class VentaResumenDto {

    private final int idVenta;              // identificador único de la venta
    private final String fecha;             // formato yyyy-MM-dd
    private final String nombreCliente;     // nombre del cliente que compró
    private final List<String> isbns;       // libros incluidos en la venta
    private final double total;             // total pagado
    private final String estado;            // nombre del enum EstadoVenta

    /** Constructor principal con todos los campos que la tabla necesita. */
    public VentaResumenDto(int idVenta, String fecha, String nombreCliente,
                           List<String> isbns, double total, String estado) {
        this.idVenta = idVenta;
        this.fecha = fecha;
        this.nombreCliente = nombreCliente;
        this.isbns = isbns;
        this.total = total;
        this.estado = estado;
    }

    /**
     * Constructor de COMPATIBILIDAD hacia atrás: los consumidores que solo
     * necesitaban (fecha, isbns, total) siguen funcionando sin cambios.
     * Asigna valores por defecto a los campos nuevos.
     */
    public VentaResumenDto(String fecha, List<String> isbns, double total) {
        this(0, fecha, "", isbns, total, "");
    }

    public int getIdVenta() { return idVenta; }
    public String getFecha() { return fecha; }
    public String getNombreCliente() { return nombreCliente; }
    public List<String> getIsbns() { return isbns; }
    public double getTotal() { return total; }
    public String getEstado() { return estado; }
}
