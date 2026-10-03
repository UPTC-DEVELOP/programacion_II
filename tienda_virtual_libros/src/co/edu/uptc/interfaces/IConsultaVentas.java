package co.edu.uptc.interfaces;

import java.util.List;

import co.edu.uptc.modelo.dto.VentaResumenDto;



/**
 * INTERFAZ IConsultaVentas  (paquete: interfaces)
 * ---------------------------------------------------------------------------
 * Este módulo NO crea ventas (eso lo hace el módulo de carrito/compra de otro
 * compañero). Solo necesita CONSULTARLAS para:
 *   - RF03: saber si un libro tiene ventas antes de eliminarlo,
 *   - Dashboard y Reportes.
 * Al depender de una interfaz, el día que el módulo de compras exista basta
 * con reemplazar la implementación temporal (ConsultaVentasMemoria) por la real.
 */
public interface IConsultaVentas {

    boolean libroTieneVentas(String isbn);

    List<VentaResumenDto> listarVentas();
}