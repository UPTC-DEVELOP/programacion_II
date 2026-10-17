package co.edu.uptc.modelo;

import java.time.LocalDate;
import java.util.List;

import co.edu.uptc.modelo.enums.EstadoVenta;

/**
 * ENTIDAD Venta  (paquete: modelo)
 * ---------------------------------------------------------------------------
 * Representa una venta COMPLETADA (o en otro estado) registrada en la tienda.
 *
 * ¿POR QUÉ ESTA ENTIDAD EXISTE AQUÍ SI EL MÓDULO DE CARRITO ES DE OTRO FASE?
 *  - El Dashboard del administrador NECESITA mostrar:
 *      * Total de ventas realizadas
 *      * Ingresos totales
 *      * Ventas recientes (ID, Fecha, Cliente, Total, Estado)
 *    Sin una entidad que represente la venta, no hay de dónde leer esos datos.
 *  - Como aún no hay módulo de checkout/compra definitivo, esta entidad es la
 *    BASE sobre la que ese módulo futuro podrá escribir; por ahora se puebla
 *    con datos de ejemplo (semillas) en LocalVenta, igual que LocalLibro.
 *
 * ENCAPSULAMIENTO:
 *  - Todos los atributos son privados; el acceso es solo vía getters/setters.
 *  - La fecha es LocalDate (inmutable) para evitar efectos secundarios.
 */
public class Venta {

    private int idVenta;
    private int idCliente;               // referencia al cliente que compró
    private String nombreCliente;        // denormalizado: la tabla necesita el nombre sin join
    private LocalDate fecha;             // fecha de la compra
    private List<String> isbns;          // ISBNs de los libros comprados
    private double total;                // total pagado (ya con IVA y descuento aplicados)
    private EstadoVenta estado;          // COMPLETADA / PENDIENTE / CANCELADA

    public Venta(int idVenta, int idCliente, String nombreCliente, LocalDate fecha,
                 List<String> isbns, double total, EstadoVenta estado) {
        this.idVenta = idVenta;
        this.idCliente = idCliente;
        this.nombreCliente = nombreCliente;
        this.fecha = fecha;
        this.isbns = isbns;
        this.total = total;
        this.estado = estado;
    }

    // -------------------------- Getters y Setters --------------------------

    public int getIdVenta() { return idVenta; }
    public void setIdVenta(int idVenta) { this.idVenta = idVenta; }

    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }

    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public List<String> getIsbns() { return isbns; }
    public void setIsbns(List<String> isbns) { this.isbns = isbns; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public EstadoVenta getEstado() { return estado; }
    public void setEstado(EstadoVenta estado) { this.estado = estado; }
}
