package co.edu.uptc.modelo.dto;

import java.util.List;

/**
 * DTO DashboardKpiDto  (paquete: modelo.dto)
 * ---------------------------------------------------------------------------
 * Agrupa TODOS los indicadores (KPIs) que el panel Dashboard del
 * administrador necesita mostrar en una sola llamada al servicio.
 *
 * ¿POR QUÉ UN SOLO DTO Y NO TRES MÉTODOS SEPARADOS?
 * 
 *  - Evita N viajes a la capa de negocio para refrescar el panel (performance
 *    conceptual y menos acoplamiento: el panel pide TODO de una vez).
 *  - Si mañana se agrega un cuarto KPI, se agrega un atributo aquí y el DTO
 *    lo transporta sin cambiar la firma de los métodos del servicio (OCP).
 *  - Es INMUTABLE (solo getters): los DTOs de lectura no deben ser
 *    modificables por el consumidor (encapsulamiento).
 */
public class DashboardKpiDto {

    private final int totalLibrosInventario;   // cantidad total de libros en stock
    private final int totalVentasRealizadas;   // número de ventas registradas
    private final double ingresosTotales;      // suma de todos los totales de venta
    private final List<VentaResumenDto> ventasRecientes; // últimas ventas para la tabla

    public DashboardKpiDto(int totalLibrosInventario, int totalVentasRealizadas,
                           double ingresosTotales, List<VentaResumenDto> ventasRecientes) {
        this.totalLibrosInventario = totalLibrosInventario;
        this.totalVentasRealizadas = totalVentasRealizadas;
        this.ingresosTotales = ingresosTotales;
        this.ventasRecientes = ventasRecientes;
    }

    public int getTotalLibrosInventario() { return totalLibrosInventario; }
    public int getTotalVentasRealizadas() { return totalVentasRealizadas; }
    public double getIngresosTotales() { return ingresosTotales; }
    public List<VentaResumenDto> getVentasRecientes() { return ventasRecientes; }
}
