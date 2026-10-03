package co.edu.uptc.persistencia;

import java.util.ArrayList;
import java.util.List;

import co.edu.uptc.interfaces.IConsultaVentas;
import co.edu.uptc.modelo.dto.VentaResumenDto;



/**
 * CLASE ConsultaVentasMemoria  (paquete: persistencia)  implements IConsultaVentas
 * ---------------------------------------------------------------------------
 * IMPLEMENTACIÓN TEMPORAL de la consulta de ventas. Las ventas las genera el
 * módulo de carrito/compra (otro compañero) y todavía no existe, así que aquí
 * se mantiene una lista de ventas que arranca VACÍA.
 *
 * Utilidad: permite PROBAR la regla de RF03 ("no eliminar libros con ventas")
 * llamando a registrarVenta(...) desde una prueba, sin depender de nadie.
 * Cuando el módulo de compras exista, se reemplaza por su implementación real
 * de IConsultaVentas (DIP) sin modificar GestionLibro ni GestionReporte.
 */
public class ConsultaVentasMemoria implements IConsultaVentas {

    private final List<VentaResumenDto> ventas = new ArrayList<>();

    /** Solo para pruebas/demostración: simula que el módulo de compras registró una venta. */
    public void registrarVenta(VentaResumenDto venta) {
        ventas.add(venta);
    }

    @Override
    public boolean libroTieneVentas(String isbn) {
        for (VentaResumenDto v : ventas) {
            if (v.getIsbns().contains(isbn)) return true;
        }
        return false;
    }

    @Override
    public List<VentaResumenDto> listarVentas() {
        return new ArrayList<>(ventas);
    }
}
