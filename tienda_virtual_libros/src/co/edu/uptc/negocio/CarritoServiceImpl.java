package co.edu.uptc.negocio;

import java.util.ArrayList;
import java.util.List;

import co.edu.uptc.excepciones.CantidadInvalidaException;
import co.edu.uptc.excepciones.ClienteNoEncontradoException;
import co.edu.uptc.excepciones.LibroNoEncontradoException;
import co.edu.uptc.excepciones.ReglaNegocioException;
import co.edu.uptc.excepciones.StockInsuficienteException;
import co.edu.uptc.interfaces.ICarritoRepository;
import co.edu.uptc.interfaces.ICarritoService;
import co.edu.uptc.interfaces.IGestionCliente;
import co.edu.uptc.interfaces.IGestionLibro;
import co.edu.uptc.modelo.CarritoCompras;
import co.edu.uptc.modelo.ItemCarrito;
import co.edu.uptc.modelo.dto.ClienteDto;
import co.edu.uptc.modelo.dto.ItemCarritoDto;
import co.edu.uptc.modelo.dto.LibroDto;
import co.edu.uptc.modelo.dto.TotalesCarritoDto;

/**
 * IMPLEMENTACIÓN del servicio (capa de NEGOCIO) del carrito de compras.
 * Implementa ICarritoService: la GUI solo conoce la interfaz (DIP).
 *
 * DEPENDENCIAS INYECTADAS POR CONSTRUCTOR (SOLID / DIP):
 *  - ICarritoRepository: persiste los carritos (hoy en memoria, luego JSON).
 *  - IGestionLibro      : valida ISBN en catálogo y consulta precio/stock/categoría (RF10).
 *  - IGestionCliente    : valida que el cliente exista ("sesión") y obtiene su
 *                         TipoCliente para el descuento Premium (RF10/RF13).
 *  - NINGUNA dependencia se newea dentro de la clase: esto permite reemplazar
 *    cada una por un MOCK en las pruebas unitarias ( Mockito ) sin tocar el código.
 *
 * NOTA sobre "ILibroService" e "IClienteService" de la consigna:
 *  - En ESTE proyecto esas abstracciones YA existen con los nombres IGestionLibro
 *    e IGestionCliente. Crear interfaces paralelas duplicaría contratos (violación
 *    DRY); por eso se inyectan las existentes, que cumplen exactamente el rol pedido.
 *
 * NOTA sobre la validación de "sesión" (RF10):
 *  - La verificación estricta de sesión activa vive en IGestionSeguridad y es
 *    responsabilidad de la GUI (solo muestra el carrito a un usuario logueado).
 *    Al servicio le corresponde validar que el idCliente exista en el sistema;
 *    si no existe, se lanza ClienteNoEncontradoException.
 */
public class CarritoServiceImpl implements ICarritoService {

    private final ICarritoRepository carritoRepository;
    private final IGestionLibro libroService;
    private final IGestionCliente clienteService;

    /**
     * CONSTRUCTOR CON INYECCIÓN DE DEPENDENCIAS (SOLID / DIP).
     * Las tres dependencias son ABSTRACCIONES (interfaces), no clases concretas:
     * el servicio no sabe ni debe saber cómo se persisten los datos ni de dónde
     * salen los libros/clientes. Eso permite cambiar implementaciones (p. ej.
     * CarritoRepositoryJson) sin modificar NI UNA línea de esta clase.
     */
    public CarritoServiceImpl(ICarritoRepository carritoRepository,
                              IGestionLibro libroService,
                              IGestionCliente clienteService) {
        this.carritoRepository = carritoRepository;
        this.libroService = libroService;
        this.clienteService = clienteService;
    }

    // ----------------------------- RF10: AGREGAR -----------------------------

    @Override
    public ItemCarritoDto agregarLibro(int idCliente, String isbn, int cantidad) throws ReglaNegocioException {
        // 1) Validación barata primero: cantidad > 0 (RF10). Sin dependencias externas.
        validarCantidadPositiva(cantidad);

        // 2) Validar que el cliente exista ("sesión" válida para el servicio).
        validarClienteExistente(idCliente);

        // 3) Validar que el ISBN exista en el catálogo (RF10).
        LibroDto libroDto = libroService.consultarPorIsbn(isbn);
        if (libroDto == null) {
            throw new LibroNoEncontradoException("El libro con ISBN " + isbn + " no existe en el catálogo.");
        }

        // 4) Obtener (o crear) el carrito del cliente.
        CarritoCompras carrito = obtenerOCrearCarrito(idCliente);

        // 5) Validar stock: considera lo YA acumulado en el carrito para ese ISBN.
        ItemCarrito existente = carrito.buscarItemPorIsbn(isbn);
        int cantidadYaEnCarrito = (existente == null) ? 0 : existente.getCantidad();
        int cantidadTotalSolicitada = cantidadYaEnCarrito + cantidad;
        if (cantidadTotalSolicitada > libroDto.getStock()) {
            throw new StockInsuficienteException(
                    "Stock insuficiente. Se solicitaron " + cantidadTotalSolicitada
                            + " unidades en total, pero solo hay " + libroDto.getStock() + " disponibles.",
                    cantidadTotalSolicitada, libroDto.getStock());
        }

        // 6) Determinar la tasa de IVA según la categoría del libro (RF10) usando
        //    la ESTRATEGIA CalculadoraImpuesto (patrón Estrategia, OCP).
        double tasaIva = CalculadoraImpuesto.porCategoria(libroDto.getCategoria()).getTasa();

        // 7) Acumular si el ISBN ya estaba; si no, crear la línea nueva (RF10).
        if (existente != null) {
            // setCantidad() de ItemCarrito recalcula solo subtotal e IVA (Fase 1).
            existente.setCantidad(cantidadTotalSolicitada);
        } else {
            ItemCarrito nuevoItem = new ItemCarrito(isbn, cantidad, libroDto.getPrecioVenta(), tasaIva);
            carrito.agregarItem(nuevoItem);
        }

        // 8) Persistir el carrito actualizado (upsert por idCliente).
        carritoRepository.guardarCarrito(carrito);

        // 9) Devolver la línea resultante mapeada a DTO para la GUI.
        return mapearItemADto(carrito.buscarItemPorIsbn(isbn), libroDto.getTitulo());
    }

    // ----------------------- RF11: AUMENTAR / DISMINUIR -----------------------

    @Override
    public ItemCarritoDto aumentarCantidad(int idCliente, String isbn, int cantidadAdicional) throws ReglaNegocioException {
        validarCantidadPositiva(cantidadAdicional);
        validarClienteExistente(idCliente);

        CarritoCompras carrito = carritoRepository.obtenerCarritoPorCliente(idCliente);
        ItemCarrito item = buscarItemEnCarrito(carrito, isbn);

        // Validar stock contra el TOTAL resultante (lo que ya tiene + lo que agrega).
        LibroDto libroDto = libroService.consultarPorIsbn(isbn);
        if (libroDto == null) {
            throw new LibroNoEncontradoException("El libro con ISBN " + isbn + " no existe en el catálogo.");
        }
        int cantidadTotalSolicitada = item.getCantidad() + cantidadAdicional;
        if (cantidadTotalSolicitada > libroDto.getStock()) {
            throw new StockInsuficienteException(
                    "Stock insuficiente. Se solicitaron " + cantidadTotalSolicitada
                            + " unidades en total, pero solo hay " + libroDto.getStock() + " disponibles.",
                    cantidadTotalSolicitada, libroDto.getStock());
        }

        item.setCantidad(cantidadTotalSolicitada); // recalcula subtotal e IVA
        carritoRepository.guardarCarrito(carrito);
        return mapearItemADto(item, libroDto.getTitulo());
    }

    @Override
    public ItemCarritoDto disminuirCantidad(int idCliente, String isbn, int cantidadAReducir) throws ReglaNegocioException {
        validarCantidadPositiva(cantidadAReducir);
        validarClienteExistente(idCliente);

        CarritoCompras carrito = carritoRepository.obtenerCarritoPorCliente(idCliente);
        ItemCarrito item = buscarItemEnCarrito(carrito, isbn);

        // Se recalcula la nueva cantidad; se usa max(0, ...) para que pedir bajar
        // "de más" nunca deje cantidades negativas (estado inválido).
        int nuevaCantidad = Math.max(0, item.getCantidad() - cantidadAReducir);
        item.setCantidad(nuevaCantidad); // recalcula subtotal e IVA (si es 0, ambos quedan en 0.0)

        carritoRepository.guardarCarrito(carrito);

        // Si la cantidad llegó a 0, el DTO resultante lo refleja: la GUI puede
        // SUGERIR la eliminación (RF11). El servicio NO borra solo (eso es RF12
        // y requiere confirmación del usuario en la capa de presentación).
        return mapearItemADto(item, tituloDeLibro(isbn));
    }

    // ----------------------------- RF12: ELIMINAR -----------------------------

    @Override
    public void eliminarItem(int idCliente, String isbn) throws ReglaNegocioException {
        validarClienteExistente(idCliente);

        CarritoCompras carrito = carritoRepository.obtenerCarritoPorCliente(idCliente);
        ItemCarrito item = buscarItemEnCarrito(carrito, isbn);

        // eliminarItemPorIsbn() de CarritoCompras remueve la línea (Fase 1).
        carrito.eliminarItemPorIsbn(isbn);
        carritoRepository.guardarCarrito(carrito);
        // La CONFIRMACIÓN previa a este borrado es responsabilidad de la GUI (RF12).
        // 'item' solo se usó para validar que existía antes de borrar.
    }

    // ------------------------- RF13: CALCULAR TOTALES -------------------------

    @Override
    public TotalesCarritoDto calcularTotales(int idCliente) throws ReglaNegocioException {
        validarClienteExistente(idCliente);

        CarritoCompras carrito = carritoRepository.obtenerCarritoPorCliente(idCliente);
        // Carrito inexistente o vacío => todos los totales en 0.0 (estado válido).
        if (carrito == null || carrito.estaVacio()) {
            return new TotalesCarritoDto(0.0, 0.0, 0.0, 0.0);
        }

        double subtotalGeneral = carrito.calcularSubtotalGeneral();
        double totalImpuestos = carrito.calcularTotalImpuestos();

        // El descuento se calcula con la ESTRATEGIA adecuada al TipoCliente (RF13).
        // Se usa el enum TipoCliente (que trae el DTO) y NO la entidad Cliente,
        // para no acoplar el servicio a los mappers de la capa de persistencia.
        ClienteDto clienteDto = clienteService.buscarPorId(idCliente);
        double descuento = CalculadoraDescuento
                .porTipoCliente(clienteDto.getTipoCliente())
                .calcularDescuento(subtotalGeneral);

        double totalFinal = subtotalGeneral + totalImpuestos - descuento;

        return new TotalesCarritoDto(redondear(subtotalGeneral), redondear(totalImpuestos),
                redondear(descuento), redondear(totalFinal));
    }

    // --------------------------- LECTURA PARA LA GUI ---------------------------

    @Override
    public List<ItemCarritoDto> obtenerItems(int idCliente) {
        List<ItemCarritoDto> resultado = new ArrayList<>();
        CarritoCompras carrito = carritoRepository.obtenerCarritoPorCliente(idCliente);
        if (carrito == null) {
            return resultado; // sin carrito => lista vacía (no es un error)
        }
        for (ItemCarrito item : carrito.obtenerItems()) {
            resultado.add(mapearItemADto(item, tituloDeLibro(item.getIsbn())));
        }
        return resultado;
    }

    // ----------------------------- MÉTODOS AUXILIARES -----------------------------

    /** Valida la regla "cantidad > 0" del RF10 y RF11. */
    private void validarCantidadPositiva(int cantidad) throws CantidadInvalidaException {
        if (cantidad <= 0) {
            throw new CantidadInvalidaException("La cantidad debe ser un número entero mayor que cero.");
        }
    }

    /**
     * Valida que el idCliente corresponda a un cliente existente en el sistema.
     * Esta es la validación de "sesión" que le corresponde al SERVICIO (ver
     * comentario principal de la clase).
     */
    private void validarClienteExistente(int idCliente) throws ClienteNoEncontradoException {
        if (clienteService.buscarPorId(idCliente) == null) {
            throw new ClienteNoEncontradoException(
                    "No existe un cliente con identificador " + idCliente + ". Sesión inválida.");
        }
    }

    /**
     * Devuelve el carrito del cliente; si no existe, crea uno NUEVO vacío (aún
     * sin persistir: se persiste cuando tenga al menos un ítem).
     * Como cada cliente tiene UN carrito, se usa el idCliente como idCarrito
     * técnico también (suficiente mientras no haya persistencia en JSON).
     */
    private CarritoCompras obtenerOCrearCarrito(int idCliente) {
        CarritoCompras carrito = carritoRepository.obtenerCarritoPorCliente(idCliente);
        if (carrito == null) {
            carrito = new CarritoCompras(idCliente, idCliente);
        }
        return carrito;
    }

    /**
     * Busca un ítem por ISBN dentro del carrito; si el carrito es null o el
     * ISBN no está, lanza ReglaNegocioException con mensaje listo para la GUI.
     */
    private ItemCarrito buscarItemEnCarrito(CarritoCompras carrito, String isbn) throws ReglaNegocioException {
        if (carrito == null) {
            throw new ReglaNegocioException("El cliente no tiene un carrito de compras.");
        }
        ItemCarrito item = carrito.buscarItemPorIsbn(isbn);
        if (item == null) {
            throw new ReglaNegocioException("El libro con ISBN " + isbn + " no está en el carrito.");
        }
        return item;
    }

    /**
     * Consulta el título de un ISBN en el catálogo para completar el DTO que
     * verá la GUI. Si el libro ya no existe (borrado del catálogo después de
     * agregarlo al carrito), se devuelve cadena vacía en vez de fallar.
     */
    private String tituloDeLibro(String isbn) {
        LibroDto libroDto = libroService.consultarPorIsbn(isbn);
        return (libroDto != null) ? libroDto.getTitulo() : "";
    }

    /**
     * Convierte la entidad ItemCarrito en el ItemCarritoDto que consumes la GUI.
     * Centraliza el mapeo para no repetirlo en cada método del servicio.
     */
    private ItemCarritoDto mapearItemADto(ItemCarrito item, String titulo) {
        if (item == null) {
            return null;
        }
        return new ItemCarritoDto(item.getIsbn(), titulo, item.getCantidad(),
                item.getPrecioUnitario(), item.getSubtotal(), item.getIva());
    }

    /** Redondea a 2 decimales (precisión monetaria del RF13). */
    private double redondear(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }
}
