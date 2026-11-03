package co.edu.uptc.interfaces;

import java.util.List;

import co.edu.uptc.modelo.Venta;

/**
 * INTERFAZ IVentaRepository  (paquete: interfaces)
 * ---------------------------------------------------------------------------
 * CONTRATO de PERSISTENCIA de las ventas. La capa de negocio (GestionDashboard)
 * solo conoce ESTA ABSTRACCIÓN, nunca la implementación concreta (DIP).
 *
 * ¿POR QUÉ EXISTE?
 *  - El dashboard necesita LEER ventas (total, ingresos, últimas N).
 *  - Cuando haya módulo JSON o JDBC, se crea otra clase que implemente esta
 *    interfaz y se cambia UNA línea en AppLibros (OCP).
 *
 * MÉTODOS:
 *  - listarTodas: retorna todas las ventas (para reportes completos).
 *  - listarRecientes: retorna las últimas N ventas (para la tabla del dashboard).
 *  - contarVentas: número total de ventas (KPI "Total de ventas realizadas").
 *  - sumarIngresos: suma de los totales de todas las ventas (KPI "Ingresos Totales").
 */
public interface IVentaRepository {

    /** Retorna TODAS las ventas registradas. */
    List<Venta> listarTodas();

    /** Retorna las últimas `cantidad` ventas, ordenadas de más reciente a más antigua. */
    List<Venta> listarRecientes(int cantidad);

    /** Cuenta el número total de ventas registradas. */
    int contarVentas();

    /** Suma el total pagado de todas las ventas (ingresos brutos). */
    double sumarIngresos();
}
