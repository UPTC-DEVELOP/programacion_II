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
import co.edu.uptc.interfaces.ILibroRepositorio;
import co.edu.uptc.modelo.CarritoCompras;
import co.edu.uptc.modelo.ItemCarrito;
import co.edu.uptc.modelo.Libro;
import co.edu.uptc.modelo.dto.ClienteDto;
import co.edu.uptc.modelo.dto.ItemCarritoDto;
import co.edu.uptc.modelo.dto.TotalesCarritoDto;

/**
 * IMPLEMENTACIÓN del servicio (capa de NEGOCIO) del carrito de compras.
 * Implementa ICarritoService: la GUI solo conoce la interfaz (DIP).
 *
 * DEPENDENCIAS INYECTADAS POR CONSTRUCTOR (SOLID / DIP):
 *  - ICarritoRepository: persiste los carritos en memoria o en JSON.
 *  - ILibroRepositorio  : valida la existencia del ISBN y consulta precio,
 *                         stock y formato en el catálogo (RF10).
 *  - IGestionCliente    : valida que el cliente exista ("sesión") y obtiene su
 *                         TipoCliente para el descuento Premium (RF10/RF13).
 *  - NINGUNA dependencia se newea dentro de la clase: esto permite reemplazar
 *    cada una por un MOCK en las pruebas unitarias sin modificar esta clase.
 *
 * NOTA sobre la validación de "sesión" (RF10):
 *  - La verificación estricta de sesión activa vive en IGestionSeguridad y es
 *    responsabilidad de la GUI (solo muestra el carrito a un usuario logueado).
 *    Al servicio le corresponde validar que el idCliente exista en el sistema;
 *    si no existe, se lanza ClienteNoEncontradoException.
 */
public class CarritoServiceImpl implements ICarritoService {

    private final ICarritoRepository carritoRepository;
    private final ICarritoRepository carritoRepositoryJson;
    private final ILibroRepositorio libroRepository;
    private final IGestionCliente clienteService;

    /**
     * CONSTRUCTOR CON INYECCIÓN DE DEPENDENCIAS (SOLID / DIP).
     * Las tres dependencias son ABSTRACCIONES (interfaces), no clases concretas:
     * el servicio no sabe ni debe saber cómo se persisten los datos ni de dónde
     * salen los libros/clientes. Eso permite cambiar implementaciones (p. ej.
     * CarritoJsonRepository) sin modificar NI UNA línea de esta clase.
     */
    public CarritoServiceImpl(ICarritoRepository carritoRepository,
                              ILibroRepositorio libroRepository,
                              IGestionCliente clienteService) {
        this(carritoRepository, libroRepository, clienteService, null);
    }

    /*modificacion:Brayan Panqueva*/
    public CarritoServiceImpl(ICarritoRepository carritoRepository,
                              ILibroRepositorio libroRepository,
                              IGestionCliente clienteService,
                              ICarritoRepository carritoRepositoryJson) {
        this.carritoRepository = carritoRepository;
        this.libroRepository = libroRepository;
        this.clienteService = clienteService;
        this.carritoRepositoryJson = carritoRepositoryJson;
    }

    // ----------------------------- RF10: AGREGAR -----------------------------

    @Override
    public ItemCarritoDto agregarLibro(int idCliente, String isbn, int cantidad) throws ReglaNegocioException {
        // 1) Validación barata primero: cantidad > 0 (RF10). Sin dependencias externas.
        validarCantidadPositiva(cantidad);

        // 2) Validar que el cliente exista ("sesión" válida para el servicio).
        ClienteDto clienteDto = validarClienteExistente(idCliente);

        // 3) Validar que el ISBN exista en el catálogo (RF10).
        Libro libro = libroRepository.consultarPorIsbn(isbn);
        if (libro == null) {
            throw new LibroNoEncontradoException("El libro con ISBN " + isbn + " no existe en el catálogo.");
        }

        // 4) Obtener (o crear) el carrito del cliente.
        CarritoCompras carrito = obtenerOCrearCarrito(idCliente);
        carrito.setTipoCliente(clienteDto.getTipoCliente());

        // 5) Validar stock: considera lo YA acumulado en el carrito para ese ISBN.
        ItemCarrito existente = carrito.buscarItemPorIsbn(isbn);
        int cantidadYaEnCarrito = (existente == null) ? 0 : existente.getCantidad();
        int cantidadTotalSolicitada = cantidadYaEnCarrito + cantidad;
        if (cantidadTotalSolicitada > libro.getStock()) {
            throw new StockInsuficienteException(
                    "Stock insuficiente. Se solicitaron " + cantidadTotalSolicitada
                            + " unidades en total, pero solo hay " + libro.getStock() + " disponibles.",
                    cantidadTotalSolicitada, libro.getStock());
        }

        // 6) Determinar la tasa de IVA según el formato del libro (RF10) usando
        //    la ESTRATEGIA CalculadoraImpuesto (patrón Estrategia, OCP).
        double tasaIva = CalculadoraImpuesto.porFormato(libro.getFormato()).getTasa();

        // 7) Acumular si el ISBN ya estaba; si no, crear la línea nueva (RF10).
        if (existente != null) {
            // setCantidad() de ItemCarrito recalcula solo subtotal e IVA (Fase 1).
            existente.setCantidad(cantidadTotalSolicitada);
            if (existente.getTitulo() == null) {
                existente.setTitulo(libro.getTitulo());
            }
        } else {
            ItemCarrito nuevoItem = new ItemCarrito(isbn, cantidad, libro.getPrecioVenta(), tasaIva);
            nuevoItem.setTitulo(libro.getTitulo());
            carrito.agregarItem(nuevoItem);
        }

        // 8) Persistir el carrito actualizado (upsert por idCliente).
        guardarCarrito(carrito);

        // 9) Devolver la línea resultante mapeada a DTO para la GUI.
        return mapearItemADto(carrito.buscarItemPorIsbn(isbn), libro.getTitulo());
    }

    // ----------------------- RF11: AUMENTAR / DISMINUIR -----------------------

    @Override
    public ItemCarritoDto aumentarCantidad(int idCliente, String isbn, int cantidadAdicional) throws ReglaNegocioException {
        validarCantidadPositiva(cantidadAdicional);
        ClienteDto clienteDto = validarClienteExistente(idCliente);

        CarritoCompras carrito = carritoRepository.obtenerCarritoPorCliente(idCliente);
        ItemCarrito item = buscarItemEnCarrito(carrito, isbn);
        carrito.setTipoCliente(clienteDto.getTipoCliente());

        // Validar stock contra el TOTAL resultante (lo que ya tiene + lo que agrega).
        Libro libro = libroRepository.consultarPorIsbn(isbn);
        if (libro == null) {
            throw new LibroNoEncontradoException("El libro con ISBN " + isbn + " no existe en el catálogo.");
        }
        int cantidadTotalSolicitada = item.getCantidad() + cantidadAdicional;
        if (cantidadTotalSolicitada > libro.getStock()) {
            throw new StockInsuficienteException(
                    "Stock insuficiente. Se solicitaron " + cantidadTotalSolicitada
                            + " unidades en total, pero solo hay " + libro.getStock() + " disponibles.",
                    cantidadTotalSolicitada, libro.getStock());
        }

        item.setCantidad(cantidadTotalSolicitada); // recalcula subtotal e IVA
        if (item.getTitulo() == null) {
            item.setTitulo(libro.getTitulo());
        }
        guardarCarrito(carrito);
        return mapearItemADto(item, libro.getTitulo());
    }

    @Override
    public ItemCarritoDto disminuirCantidad(int idCliente, String isbn, int cantidadAReducir) throws ReglaNegocioException {
        validarCantidadPositiva(cantidadAReducir);
        ClienteDto clienteDto = validarClienteExistente(idCliente);

        CarritoCompras carrito = carritoRepository.obtenerCarritoPorCliente(idCliente);
        ItemCarrito item = buscarItemEnCarrito(carrito, isbn);
        carrito.setTipoCliente(clienteDto.getTipoCliente());

        // Se recalcula la nueva cantidad; se usa max(0, ...) para que pedir bajar
        // "de más" nunca deje cantidades negativas (estado inválido).
        int nuevaCantidad = Math.max(0, item.getCantidad() - cantidadAReducir);
        item.setCantidad(nuevaCantidad); // recalcula subtotal e IVA (si es 0, ambos quedan en 0.0)

        guardarCarrito(carrito);

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
        guardarCarrito(carrito);
        // La CONFIRMACIÓN previa a este borrado es responsabilidad de la GUI (RF12).
        // 'item' solo se usó para validar que existía antes de borrar.
    }

    /**
     * Elimina el carrito completo del cliente. Se propaga a las dos
     * implementaciones de persistencia para mantener sincronizados JDBC y JSON.
     */
    @Override
    public void eliminarCarrito(int idCliente) throws ReglaNegocioException {
        validarClienteExistente(idCliente);
        carritoRepository.eliminarCarrito(idCliente);
        if (carritoRepositoryJson != null) {
            carritoRepositoryJson.eliminarCarrito(idCliente);
        }
    }

    // ------------------------- RF13: CALCULAR TOTALES -------------------------

    @Override
    public TotalesCarritoDto calcularTotales(int idCliente) throws ReglaNegocioException {
        ClienteDto clienteDto = validarClienteExistente(idCliente);

        CarritoCompras carrito = carritoRepository.obtenerCarritoPorCliente(idCliente);
        // Carrito inexistente o vacío => todos los totales en 0.0 (estado válido).
        if (carrito == null || carrito.estaVacio()) {
            return new TotalesCarritoDto(0.0, 0.0, 0.0, 0.0);
        }
        carrito.setTipoCliente(clienteDto.getTipoCliente());

        double subtotalGeneral = carrito.calcularSubtotalGeneral();
        double totalImpuestos = carrito.calcularTotalImpuestos();

        // El descuento se calcula con la ESTRATEGIA adecuada al TipoCliente (RF13).
        // Se usa el enum TipoCliente (que trae el DTO) y NO la entidad Cliente,
        // para no acoplar el servicio a los mappers de la capa de persistencia.
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
            String titulo = (item.getTitulo() != null)
                    ? item.getTitulo()
                    : tituloDeLibro(item.getIsbn());
            resultado.add(mapearItemADto(item, titulo));
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
    private ClienteDto validarClienteExistente(int idCliente) throws ClienteNoEncontradoException {
        ClienteDto clienteDto = clienteService.buscarPorId(idCliente);
        if (clienteDto == null) {
            throw new ClienteNoEncontradoException(
                    "No existe un cliente con identificador " + idCliente + ". Sesión inválida.");
        }
        return clienteDto;
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
        Libro libro = libroRepository.consultarPorIsbn(isbn);
        return (libro != null) ? libro.getTitulo() : "";
    }

    /*modificacion:Brayan Panqueva*/
    private void guardarCarrito(CarritoCompras carrito) {
        carritoRepository.guardarCarrito(carrito);
        if (carritoRepositoryJson != null) {
            carritoRepositoryJson.guardarCarrito(carrito);
        }
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
