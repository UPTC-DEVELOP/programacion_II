package co.edu.uptc.interfaces;

import java.util.List;

import co.edu.uptc.modelo.dto.IngresoClienteDto;
import co.edu.uptc.modelo.dto.LibroVendidoDto;

/**
 * INTERFAZ IGestionReporte  (paquete: interfaces)
 * ---------------------------------------------------------------------------
 * CONTRATO de la capa de NEGOCIO de los reportes del administrador 
 * La presentación (PanelReportes / Evento) depende de ESTA ABSTRACCIÓN, nunca
 * de la implementación concreta GestionReporte (DIP).
 *
 * 
 *  - SRP: GestionDashboard calcula KPIs del dashboard (stock, ventas, ingresos
 *    de LocalVenta). Estos reportes tienen una RESPONSABILIDAD DISTINTA:
 *    analizar los carritos ACTIVOS de ICarritoRepository. Mezclarlos en la
 *    misma clase violaría SRP y haría crecer el acoplamiento innecesariamente.
 *  - Testeabilidad: al depender de la interfaz, se puede sustituir por un mock
 *    en pruebas sin tocar la GUI ni el resto del negocio.
 *
 * MÉTODOS:
 *  - librosMasVendidosEnCarritos: retorna los libros ordenados por cantidad
 *    total acumulada en TODOS los carritos activos.
 *  - ingresosEstimadosPorCliente: retorna, por cada cliente con carrito activo,
 *    el subtotal y total estimados de su carrito.
 */
public interface IGestionReporte {

    /** Reporte "Libros más vendidos en carritos": ordenado de mayor a menor cantidad. */
    List<LibroVendidoDto> librosMasVendidosEnCarritos();

    /** Reporte "Ingresos totales estimados por cliente": ordenado de mayor a menor total. */
    List<IngresoClienteDto> ingresosEstimadosPorCliente();
}
