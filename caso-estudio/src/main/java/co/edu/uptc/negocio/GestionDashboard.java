package co.edu.uptc.negocio;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import co.edu.uptc.interfaces.IGestionDashboard;
import co.edu.uptc.interfaces.ILibroRepositorio;
import co.edu.uptc.interfaces.IVentaRepository;
import co.edu.uptc.modelo.Venta;
import co.edu.uptc.modelo.dto.DashboardKpiDto;
import co.edu.uptc.modelo.dto.VentaResumenDto;

/**
 * Capa de NEGOCIO del dashboard administrativo.
 * Implementa IGestionDashboard: la presentación solo ve la interfaz (DIP).
 *
 * 
 *  - El dashboard necesita DOS fuentes de datos: libros (para el stock total)
 *    y ventas (para ingresos, conteo y tabla reciente).
 *  - La inyección por constructor garantiza que el servicio NO pueda existir
 *    sin sus dependencias (no hay estado nulo oculto) y facilita las pruebas
 *    futuras: se pueden pasar implementaciones falsas (mocks).
 *
 * 
 *  - No hay variación de algoritmo que polimorfizar: el cálculo de KPIs es
 *    único. Las clases abstractas (CalculadoraImpuesto, CalculadoraDescuento)
 *    se reservan para cuando SÍ hay familias de algoritmos cambiantes (IVA
 *    5% vs 19%, descuento regular vs premium). Aquí sería sobre-ingeniería.
 */
public class GestionDashboard implements IGestionDashboard {

    private final ILibroRepositorio repositorioLibro;
    private final IVentaRepository repositorioVenta;

    public GestionDashboard(ILibroRepositorio repositorioLibro, IVentaRepository repositorioVenta) {
        this.repositorioLibro = repositorioLibro;
        this.repositorioVenta = repositorioVenta;
    }

    @Override
    public DashboardKpiDto obtenerKpis() {
        // 1. Total de libros en inventario: suma del stock de TODOS los libros.
        int totalLibros = repositorioLibro.listarTodos().stream()
                .mapToInt(libro -> libro.getStock())
                .sum();

        // 2. Total de ventas realizadas: conteo directo del repositorio.
        int totalVentas = repositorioVenta.contarVentas();

        // 3. Ingresos totales: suma de los totales de cada venta.
        double ingresos = repositorioVenta.sumarIngresos();

        // 4. Ventas recientes: las últimas 8 para la tabla del dashboard.
        List<VentaResumenDto> recientes = repositorioVenta.listarRecientes(8).stream()
                .map(this::aResumen)
                .collect(Collectors.toList());

        return new DashboardKpiDto(totalLibros, totalVentas, ingresos, recientes);
    }

    @Override
    public List<VentaResumenDto> listarVentas() {
        // Se ordena de más reciente a más antiguo para que el reporte
        // muestre primero lo más nuevo, igual que el dashboard.
        return repositorioVenta.listarTodas().stream()
                .sorted(Comparator.comparing(Venta::getFecha).reversed())
                .map(this::aResumen)
                .collect(Collectors.toList());
    }

    /**
     * Convierte una entidad Venta a su DTO de presentación (patrón Mapper).
     * Se centraliza aquí para no repetir la conversión en cada método.
     */
    private VentaResumenDto aResumen(Venta venta) {
        return new VentaResumenDto(
                venta.getIdVenta(),
                venta.getFecha().toString(),          // yyyy-MM-dd
                venta.getNombreCliente(),
                venta.getIsbns(),
                venta.getTotal(),
                venta.getEstado().getNombreMostrar() // "Completada", "Pendiente", etc.
        );
    }
}
