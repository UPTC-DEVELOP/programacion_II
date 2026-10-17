package co.edu.uptc.persistencia;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import co.edu.uptc.interfaces.IVentaRepository;
import co.edu.uptc.modelo.Venta;
import co.edu.uptc.modelo.enums.EstadoVenta;

/**
 * IMPLEMENTACIÓN EN MEMORIA del repositorio de ventas.
 * Implementa IVentaRepository: la capa de negocio solo ve la interfaz (DIP).
 *
 * ¿POR QUÉ CARGA DATOS DE EJEMPLO (SEMILLAS)?
 *  - El dashboard del administrador necesita mostrar KPIs y "Ventas recientes"
 *    aunque el módulo de checkout aún no esté implementado. Sin datos de
 *    ejemplo, el dashboard aparecería vacío y no se podría demostrar la
 *    funcionalidad.
 *  - Es el mismo criterio que usa LocalLibro: precargar para que la aplicación
 *    sea demostrable desde el primer momento.
 *
 * ¿POR QUÉ ArrayList + SORTING EN VEZ DE LinkedList?
 *  - Las ventas se consultan mucho más de lo que se insertan; elArrayList
 *    ofrece mejor localidad de caché para recorridos y sorting.
 *  - El orden "más reciente primero" se calcula en listarRecientes con un
 *    Comparator sobre la fecha, que es la forma más legible y mantenible.
 *
 * NOTA: los datos viven solo en RAM; al reiniciar la aplicación se pierden.
 * Ese es el contrato de "en memoria" hasta que llegue el módulo JSON.
 */
public class LocalVenta implements IVentaRepository {

    /** Lista simple de ventas; en un sistema real sería una tabla en BD. */
    private final List<Venta> baseDeDatos = new ArrayList<>();

    /** Contador autoincremental para asignar IDs únicos a nuevas ventas. */
    private int siguienteId = 1;

    public LocalVenta() {
        cargarDatosDeEjemplo();
    }

    // -------------------------- IVentaRepository --------------------------

    @Override
    public List<Venta> listarTodas() {
        // Retorna copia defensiva: el repositorio no expone su estado interno.
        return new ArrayList<>(baseDeDatos);
    }

    @Override
    public List<Venta> listarRecientes(int cantidad) {
        if (cantidad <= 0) {
            return new ArrayList<>();
        }
        return baseDeDatos.stream()
                // Ordena de más reciente (fecha más grande) a más antiguo
                .sorted(Comparator.comparing(Venta::getFecha).reversed())
                // Limita a las primeras N
                .limit(cantidad)
                .collect(Collectors.toList());
    }

    @Override
    public int contarVentas() {
        return baseDeDatos.size();
    }

    @Override
    public double sumarIngresos() {
        return baseDeDatos.stream()
                .mapToDouble(Venta::getTotal)
                .sum();
    }

    // -------------------------- Datos de ejemplo --------------------------

    /**
     * Crea ventas de prueba con fechas relativas a HOY para que siempre
     * se vean "recientes" al ejecutar la aplicación, sin importar cuándo
     * se ejecute.
     */
    private void cargarDatosDeEjemplo() {
        LocalDate hoy = LocalDate.now();

        guardar(new Venta(siguienteId++, 2, "Ana Torres", hoy.minusDays(1),
                List.of("9780134685991"), 189000.0, EstadoVenta.COMPLETADA));

        guardar(new Venta(siguienteId++, 3, "Luis Pérez", hoy.minusDays(2),
                List.of("9788437604947", "9780451524935"), 112000.0, EstadoVenta.COMPLETADA));

        guardar(new Venta(siguienteId++, 2, "Ana Torres", hoy.minusDays(3),
                List.of("9780141439518"), 39900.0, EstadoVenta.PENDIENTE));

        guardar(new Venta(siguienteId++, 4, "María Gómez", hoy.minusDays(5),
                List.of("9788420430263"), 65100.0, EstadoVenta.COMPLETADA));

        guardar(new Venta(siguienteId++, 5, "Carlos Ruiz", hoy.minusDays(7),
                List.of("9780134685991", "9780141439518"), 227000.0, EstadoVenta.COMPLETADA));

        guardar(new Venta(siguienteId++, 3, "Luis Pérez", hoy.minusDays(10),
                List.of("9780451524935"), 47250.0, EstadoVenta.CANCELADA));

        guardar(new Venta(siguienteId++, 4, "María Gómez", hoy.minusDays(12),
                List.of("9788437604947"), 61950.0, EstadoVenta.COMPLETADA));

        guardar(new Venta(siguienteId++, 2, "Ana Torres", hoy.minusDays(15),
                List.of("9788420430263", "9780134685991"), 248000.0, EstadoVenta.COMPLETADA));
    }

    /**
     * Guarda una venta validando no-nulo y asignando ID si viene en 0.
     * En un sistema real, esta lógica estaría en el servicio; aquí se
     * simplifica porque es solo un repositorio de demostración.
     */
    public void guardar(Venta venta) {
        if (venta == null) {
            return;
        }
        if (venta.getIdVenta() == 0) {
            venta.setIdVenta(siguienteId++);
        }
        baseDeDatos.add(venta);
    }
}
