package co.edu.uptc.negocio;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.edu.uptc.interfaces.ICarritoRepository;
import co.edu.uptc.interfaces.IClienteRepositorio;
import co.edu.uptc.interfaces.IGestionLibro;
import co.edu.uptc.interfaces.IGestionReporte;
import co.edu.uptc.modelo.CarritoCompras;
import co.edu.uptc.modelo.Cliente;
import co.edu.uptc.modelo.ItemCarrito;
import co.edu.uptc.modelo.dto.IngresoClienteDto;
import co.edu.uptc.modelo.dto.LibroDto;
import co.edu.uptc.modelo.dto.LibroVendidoDto;

/**
 * Capa de NEGOCIO de los reportes del administrador 
 * Implementa IGestionReporte: la presentación solo ve la interfaz (DIP).
 *
 * 
 *  - ICarritoRepository: fuente de datos PRINCIPAL de estos reportes: contiene
 *    todos los carritos activos (con sus ItemCarrito y cantidades).
 *  - IGestionLibro: para obtener el TÍTULO y PRECIO de cada ISBN encontrado en
 *    los carritos (el carrito solo guarda el ISBN, no el título).
 *  - IClienteRepositorio: para obtener el NOMBRE del cliente dueño de cada
 *    carrito en el reporte de ingresos por cliente.
 *
 * ¿POR QUÉ NO SE USA CLASE ABSTRACTA AQUÍ?
 *  - No hay variación de algoritmo que polimorfizar: los dos reportes tienen
 *    una única fórmula de cálculo. Las clases abstractas (CalculadoraImpuesto,
 *    CalculadoraDescuento) se reservan para familias de algoritmos cambiantes.
 *    Aquí sería sobre-ingeniería (YAGNI).
 *
 * COMPLEJIDAD ALGORÍTMICA:
 *  - librosMasVendidosEnCarritos: O(N*M) donde N = carritos y M = ítems por
 *    carrito (acumula en un HashMap). Suficiente para un proyecto universitario
 *    con pocos carritos; para millones de datos se movería a una consulta SQL
 *    con GROUP BY.
 */
public class GestionReporte implements IGestionReporte {

    private final ICarritoRepository carritoRepository;
    private final IGestionLibro libroService;
    private final IClienteRepositorio clienteRepository;

    /**
     * CONSTRUCTOR CON INYECCIÓN DE DEPENDENCIAS (SOLID / DIP).
     * Las tres dependencias son ABSTRACCIONES (interfaces), no clases concretas:
     * el servicio no sabe ni debe saber cómo se persisten los datos. Eso permite
     * cambiar implementaciones (p. ej. CarritoRepositoryJson) sin modificar NI
     * UNA línea de esta clase.
     */
    public GestionReporte(ICarritoRepository carritoRepository,
                          IGestionLibro libroService,
                          IClienteRepositorio clienteRepository) {
        this.carritoRepository = carritoRepository;
        this.libroService = libroService;
        this.clienteRepository = clienteRepository;
    }

    @Override
    public List<LibroVendidoDto> librosMasVendidosEnCarritos() {
        // HashMap temporal: isbn -> cantidad acumulada en TODOS los carritos.
        Map<String, Integer> acumuladoPorIsbn = new HashMap<>();

        // 1) Recorrer TODOS los carritos activos y acumular cantidades por ISBN.
        for (CarritoCompras carrito : carritoRepository.listarTodos()) {
            for (ItemCarrito item : carrito.obtenerItems()) {
                // Se ignoran líneas con cantidad 0 (carrito en proceso de limpieza).
                if (item.getCantidad() > 0) {
                    acumuladoPorIsbn.merge(item.getIsbn(), item.getCantidad(), Integer::sum);
                }
            }
        }

        // 2) Convertir cada entrada del HashMap en un LibroVendidoDto,
        //    consultando el catálogo para obtener título y precio.
        List<LibroVendidoDto> resultado = new ArrayList<>();
        for (Map.Entry<String, Integer> entrada : acumuladoPorIsbn.entrySet()) {
            String isbn = entrada.getKey();
            int cantidad = entrada.getValue();

            LibroDto libro = libroService.consultarPorIsbn(isbn);
            String titulo = (libro != null) ? libro.getTitulo() : "(Libro no encontrado)";
            double precio = (libro != null) ? libro.getPrecioVenta() : 0.0;

            resultado.add(new LibroVendidoDto(isbn, titulo, cantidad,
                    Math.round(cantidad * precio * 100.0) / 100.0));
        }

        // 3) Ordenar de MAYOR a MENOR cantidad (el reporte es "más vendidos").
        resultado.sort(Comparator.comparingInt(LibroVendidoDto::getCantidadTotal).reversed());
        return resultado;
    }

    @Override
    public List<IngresoClienteDto> ingresosEstimadosPorCliente() {
        List<IngresoClienteDto> resultado = new ArrayList<>();

        for (CarritoCompras carrito : carritoRepository.listarTodos()) {
            // Un carrito vacío no aporta ingreso estimado: se salta.
            if (carrito.estaVacio()) {
                continue;
            }

            // Consultar el cliente dueño del carrito para nombre y tipo.
            Cliente cliente = clienteRepository.buscarPorId(carrito.getIdCliente());
            String nombre = (cliente != null) ? cliente.getNombreCompleto() : "(Cliente no encontrado)";
            String tipo = (cliente != null && cliente.getTipoCliente() != null)
                    ? cliente.getTipoCliente().getNombreMostrar() : "-";

            // Calcular subtotal y total estimados usando los MISMOS métodos del
            // aggregate CarritoCompras (polimorfismo: el descuento lo calcula
            // Cliente.calcularDescuento() según su tipo real, sin preguntar "es premium?").
            double subtotal = carrito.calcularSubtotalGeneral();
            double total = carrito.calcularTotalFinal(cliente);

            resultado.add(new IngresoClienteDto(
                    carrito.getIdCliente(),
                    nombre,
                    tipo,
                    carrito.obtenerItems().size(),
                    subtotal,
                    total));
        }

        // Ordenar de MAYOR a MENOR total estimado (los clientes más valiosos primero).
        resultado.sort(Comparator.comparingDouble(IngresoClienteDto::getTotalEstimado).reversed());
        return resultado;
    }

    /**
     * Método auxiliar (no forma parte de la interfaz): agrupa las líneas de
     * TODOS los carritos por ISBN para reutilizar la lógica de acumulación.
     * Se mantiene privado para no exponer detalles internos (encapsulamiento).
     */
    private Map<String, Integer> agruparCantidadesPorIsbn() {
        Map<String, Integer> acumulado = new HashMap<>();
        for (CarritoCompras carrito : carritoRepository.listarTodos()) {
            for (ItemCarrito item : carrito.obtenerItems()) {
                if (item.getCantidad() > 0) {
                    acumulado.merge(item.getIsbn(), item.getCantidad(), Integer::sum);
                }
            }
        }
        return acumulado;
    }
}
