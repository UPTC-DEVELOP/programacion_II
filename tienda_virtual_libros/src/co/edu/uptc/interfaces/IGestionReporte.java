package co.edu.uptc.interfaces;

import java.util.List;

import co.edu.uptc.modelo.dto.VentaResumenDto;



/**
 * INTERFAZ IGestionReporte  (paquete: interfaces)
 * ---------------------------------------------------------------------------
 * Contrato de negocio para lo que muestran el Dashboard y Reportes.
 * Está separada de IGestionLibro por el principio ISP (Segregación de
 * Interfaces): quien solo necesita reportes no queda obligado a conocer el CRUD.
 */
public interface IGestionReporte {

    /** Cantidad de títulos registrados en el catálogo. */
    int totalLibros();

    /** Cantidad total de ventas realizadas. */
    int totalVentas();

    /** Suma de los totales de todas las ventas. */
    double ingresosTotales();

    /** Últimas ventas registradas (para "Ventas recientes" del Dashboard). */
    List<VentaResumenDto> ventasRecientes(int cantidad);

    /** Ventas entre dos fechas yyyy-MM-dd (nulas = sin límite). */
    List<VentaResumenDto> ventasEntre(String desde, String hasta);
}
