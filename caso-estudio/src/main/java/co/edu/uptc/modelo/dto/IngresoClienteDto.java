package co.edu.uptc.modelo.dto;

/**
 * DTO IngresoClienteDto  (paquete: modelo.dto)
 * ---------------------------------------------------------------------------
 * Resultado del reporte "Ingresos totales estimados por cliente" 
 *
 * ¿POR QUÉ EXISTE?
 *  - El administrador necesita saber, para CADA cliente, cuánto dinero hay
 *    "atrapado" en su carrito activo (subtotal + IVA - descuento). Esto es un
 *    ingreso ESTIMADO (aún no se ha confirmado la venta), útil para priorizar
 *    seguimiento comercial a clientes con carritos grandes.
 *  - Se calcula recorriendo todos los carritos de ICarritoRepository y, para
 *    cada uno, usando los métodos de cálculo del propio CarritoCompras
 *    (calcularSubtotalGeneral, calcularTotalImpuestos, calcularDescuento).
 *  - El nombre del cliente se obtiene de IClienteRepositorio para mostrarlo
 *    en la tabla del reporte.
 *
 * ES INMUTABLE (solo getters): los DTOs de lectura no deben ser modificables.
 */
public class IngresoClienteDto {

    private final int idCliente;
    private final String nombreCliente;
    private final String tipoCliente;      // "Premium" o "Regular" (para mostrar el descuento)
    private final int cantidadItems;       // número de líneas distintas en el carrito
    private final double subtotalEstimado; // suma de subtotales SIN IVA ni descuento
    private final double totalEstimado;    // subtotal + IVA - descuento (lo que pagaría)

    public IngresoClienteDto(int idCliente, String nombreCliente, String tipoCliente,
                             int cantidadItems, double subtotalEstimado, double totalEstimado) {
        this.idCliente = idCliente;
        this.nombreCliente = nombreCliente;
        this.tipoCliente = tipoCliente;
        this.cantidadItems = cantidadItems;
        this.subtotalEstimado = subtotalEstimado;
        this.totalEstimado = totalEstimado;
    }

    public int getIdCliente() { return idCliente; }
    public String getNombreCliente() { return nombreCliente; }
    public String getTipoCliente() { return tipoCliente; }
    public int getCantidadItems() { return cantidadItems; }
    public double getSubtotalEstimado() { return subtotalEstimado; }
    public double getTotalEstimado() { return totalEstimado; }
}
