package co.edu.uptc.caso.estudio.negocio;

import co.edu.uptc.caso.estudio.model.Carrito;
import co.edu.uptc.caso.estudio.model.Cliente;
import co.edu.uptc.caso.estudio.model.Compra;
import co.edu.uptc.caso.estudio.model.DatosTransferencia;
import co.edu.uptc.caso.estudio.model.ItemCarrito;
import co.edu.uptc.caso.estudio.model.Libro;
import co.edu.uptc.caso.estudio.model.enums.MetodoPago;
import co.edu.uptc.caso.estudio.negocio.dto.PagoDto;
import co.edu.uptc.caso.estudio.persistence.IRepositorioCarrito;
import co.edu.uptc.caso.estudio.persistence.IRepositorioCompra;
import co.edu.uptc.caso.estudio.persistence.RegistradorOperacionesTxt;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// Implementación de reglas de negocio de compras con respaldo MySQL -> JSON y log TXT
public class GestionComprasImpl implements IGestionCompras {

    private static final String ID_CLIENTE_ANONIMO = "anonimo";
    private static final String REGEX_TARJETA = "\\d{16}";
    private static final String REGEX_CVV = "\\d{3,4}";
    private static final String REGEX_EXPIRACION = "(0[1-9]|1[0-2])/\\d{2}";
    private static final String REGEX_NEQUI = "3\\d{9}";
    private static final DateTimeFormatter FORMATO_EXPIRACION = DateTimeFormatter.ofPattern("MM/yy");

    private final IRepositorioCarrito repoCarritoMySQL;
    private final IRepositorioCarrito repoCarritoJson;
    private final IRepositorioCompra repoCompraMySQL;
    private final IRepositorioCompra repoCompraJson;
    private final IGestionUsuarios gestionUsuarios;
    private final IGestionLibros gestionLibros;
    private final RegistradorOperacionesTxt registradorTxt;
    private DatosTransferencia datosTransferencia;

    public GestionComprasImpl(
            IRepositorioCarrito repoCarritoMySQL,
            IRepositorioCarrito repoCarritoJson,
            IRepositorioCompra repoCompraMySQL,
            IRepositorioCompra repoCompraJson,
            IGestionUsuarios gestionUsuarios,
            IGestionLibros gestionLibros,
            RegistradorOperacionesTxt registradorTxt) {
        this.repoCarritoMySQL = repoCarritoMySQL;
        this.repoCarritoJson = repoCarritoJson;
        this.repoCompraMySQL = repoCompraMySQL;
        this.repoCompraJson = repoCompraJson;
        this.gestionUsuarios = gestionUsuarios;
        this.gestionLibros = gestionLibros;
        this.registradorTxt = registradorTxt;
    }

    // Obtiene el carrito del usuario autenticado priorizando MySQL y respaldando en JSON
    @Override
    public Carrito obtenerCarritoActual() {
        Cliente logueado = obtenerUsuarioLogueado();
        if (logueado == null) {
            return new Carrito(ID_CLIENTE_ANONIMO);
        }

        Carrito carrito = buscarCarritoPersistido(logueado.getId());
        if (carrito == null) {
            carrito = new Carrito(logueado.getId());
        }
        refrescarLibrosDelCarrito(carrito);
        return carrito;
    }

    // Consulta el carrito en MySQL y recurre al respaldo JSON si no existe o está vacío
    private Carrito buscarCarritoPersistido(String idCliente) {
        Carrito carrito = repoCarritoMySQL != null
                ? repoCarritoMySQL.obtenerCarritoPorCliente(idCliente)
                : null;
        if (carrito == null || carrito.estaVacio()) {
            Carrito respaldoJson = repoCarritoJson != null
                    ? repoCarritoJson.obtenerCarritoPorCliente(idCliente)
                    : null;
            if (respaldoJson != null && !respaldoJson.estaVacio()) {
                return respaldoJson;
            }
        }
        return carrito;
    }

    // Actualiza referencias de libros del carrito y elimina ítems que ya no existen
    private void refrescarLibrosDelCarrito(Carrito carrito) {
        if (carrito == null || carrito.getItems() == null) {
            return;
        }
        Iterator<ItemCarrito> it = carrito.getItems().iterator();
        while (it.hasNext()) {
            ItemCarrito item = it.next();
            Libro actual = buscarLibroActualizado(item);
            if (actual == null) {
                it.remove();
            } else {
                item.setLibro(actual);
            }
        }
    }

    // Agrega un libro al carrito si hay sesión activa y stock suficiente
    @Override
    public void agregarAlCarrito(Libro libro, int cantidad) {
        if (obtenerUsuarioLogueado() == null || libro == null || cantidad <= 0) {
            return;
        }

        Carrito c = obtenerCarritoActual();
        ItemCarrito existente = c.buscarItem(libro.getIsbn());
        int cantidadActual = (existente != null) ? existente.getCantidad() : 0;

        Libro libroActualizado = gestionLibros != null
                ? gestionLibros.buscarLibroPorIsbn(libro.getIsbn())
                : null;
        int stockDisponible = (libroActualizado != null)
                ? libroActualizado.getStock()
                : libro.getStock();

        if (cantidadActual + cantidad > stockDisponible) {
            return;
        }

        c.agregarItem(libroActualizado != null ? libroActualizado : libro, cantidad);
        sincronizarCarrito(c);
    }

    // Actualiza la cantidad de un ítem validando sesión, cantidad positiva y stock disponible
    @Override
    public String actualizarCantidadCarrito(String isbn, int nuevaCantidad) {
        if (obtenerUsuarioLogueado() == null) {
            return "Debe iniciar sesión primero.";
        }
        if (esVacio(isbn)) {
            return "ISBN inválido.";
        }
        if (nuevaCantidad <= 0) {
            return "La cantidad debe ser mayor a 0.";
        }

        Carrito c = obtenerCarritoActual();
        ItemCarrito item = c.buscarItem(isbn);
        if (item == null) {
            return "El libro no se encuentra en el carrito.";
        }

        Libro l = gestionLibros != null ? gestionLibros.buscarLibroPorIsbn(isbn) : null;
        if (l != null && nuevaCantidad > l.getStock()) {
            return "Stock insuficiente. Disponible: " + l.getStock();
        }

        c.actualizarCantidad(isbn, nuevaCantidad);
        sincronizarCarrito(c);
        return null;
    }

    // Elimina los libros indicados del carrito y sincroniza los repositorios
    @Override
    public boolean eliminarDelCarrito(List<String> isbns) {
        if (obtenerUsuarioLogueado() == null || isbns == null || isbns.isEmpty()) {
            return false;
        }

        Carrito c = obtenerCarritoActual();
        boolean eliminado = c.eliminarItems(isbns);
        if (eliminado) {
            sincronizarCarrito(c);
        }
        return eliminado;
    }

    // Vacía el carrito del usuario actual en MySQL y JSON
    @Override
    public void vaciarCarrito() {
        Cliente logueado = obtenerUsuarioLogueado();
        if (logueado != null) {
            if (repoCarritoMySQL != null) {
                repoCarritoMySQL.vaciarCarrito(logueado.getId());
            }
            if (repoCarritoJson != null) {
                repoCarritoJson.vaciarCarrito(logueado.getId());
            }
        }
    }

    // Valida y almacena temporalmente los datos del método de pago seleccionado
    @Override
    public String registrarPago(PagoDto pago) {
        if (pago == null) {
            return "Datos de pago inválidos.";
        }
        if (obtenerCarritoActual().estaVacio()) {
            return "El carrito está vacío.";
        }

        this.datosTransferencia = null;
        if (MetodoPago.TARJETA.getEtiqueta().equalsIgnoreCase(pago.getMetodo())) {
            return validarPagoTarjeta(pago);
        }
        if (MetodoPago.TRANSFERENCIA.getEtiqueta().equalsIgnoreCase(pago.getMetodo())) {
            return validarPagoTransferencia(pago);
        }
        return "Método de pago no válido.";
    }

    // Valida campos, formato y fecha de expiración para pago con tarjeta
    private String validarPagoTarjeta(PagoDto pago) {
        if (esVacio(pago.getBanco())) {
            return "Seleccione un banco.";
        }
        if (pago.getNumeroTarjeta() == null || !pago.getNumeroTarjeta().matches(REGEX_TARJETA)) {
            return "Número de tarjeta inválido (16 dígitos).";
        }
        if (pago.getCvv() == null || !pago.getCvv().matches(REGEX_CVV)) {
            return "CVV inválido (3 o 4 dígitos).";
        }
        if (pago.getExpiracion() == null || !pago.getExpiracion().matches(REGEX_EXPIRACION)) {
            return "Fecha de expiración inválida (MM/AA).";
        }

        YearMonth vencimiento = parsearExpiracion(pago.getExpiracion());
        if (vencimiento == null) {
            return "Fecha de expiración inválida (MM/AA).";
        }
        if (vencimiento.isBefore(YearMonth.now())) {
            return "La tarjeta se encuentra vencida.";
        }

        this.datosTransferencia = new DatosTransferencia(
                pago.getBanco(),
                pago.getNumeroTarjeta(),
                pago.getCvv(),
                pago.getExpiracion(),
                null);
        return null;
    }

    // Convierte MM/yy de forma segura ante entradas malformadas
    private YearMonth parsearExpiracion(String expiracion) {
        try {
            return YearMonth.parse(expiracion, FORMATO_EXPIRACION);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    // Valida número celular colombiano para pagos vía Nequi
    private String validarPagoTransferencia(PagoDto pago) {
        if (pago.getNumeroNequi() == null || !pago.getNumeroNequi().matches(REGEX_NEQUI)) {
            return "Número de Nequi inválido (10 dígitos empezando por 3).";
        }
        this.datosTransferencia = new DatosTransferencia(
                DatosTransferencia.NEQUI,
                null,
                null,
                null,
                pago.getNumeroNequi());
        return null;
    }

    // Verifica disponibilidad, registra la compra, descuenta stock y limpia el pago temporal
    @Override
    public boolean finalizarCompra() {
        Cliente logueado = obtenerUsuarioLogueado();
        if (logueado == null) {
            return false;
        }

        Carrito c = obtenerCarritoActual();
        if (c == null || c.estaVacio() || datosTransferencia == null) {
            return false;
        }
        if (!tieneStockSuficiente(c)) {
            return false;
        }

        String infoPago = datosTransferencia.getBanco();
        boolean compraMySQL = repoCompraMySQL != null
                && repoCompraMySQL.registrarCompra(logueado, c, infoPago);
        boolean compraJson = repoCompraJson != null
                && repoCompraJson.registrarCompra(logueado, c, infoPago);

        this.datosTransferencia = null;
        if (!compraMySQL && !compraJson) {
            return false;
        }

        descontarStockCarrito(c);
        registrarOperacionCompra(logueado, c, infoPago);
        return true;
    }

    // Comprueba que cada ítem del carrito tenga stock disponible antes de confirmar la compra
    private boolean tieneStockSuficiente(Carrito carrito) {
        if (carrito == null || carrito.getItems() == null || carrito.getItems().isEmpty()) {
            return false;
        }
        for (ItemCarrito item : carrito.getItems()) {
            if (item == null || item.getLibro() == null || item.getCantidad() <= 0) {
                return false;
            }
            Libro actual = gestionLibros != null
                    ? gestionLibros.buscarLibroPorIsbn(item.getLibro().getIsbn())
                    : item.getLibro();
            if (actual == null || actual.getStock() < item.getCantidad()) {
                return false;
            }
        }
        return true;
    }

    // Descuenta del inventario la cantidad comprada de cada libro
    private void descontarStockCarrito(Carrito carrito) {
        if (gestionLibros == null || carrito == null || carrito.getItems() == null) {
            return;
        }
        for (ItemCarrito item : carrito.getItems()) {
            if (item != null && item.getLibro() != null && item.getCantidad() > 0) {
                gestionLibros.ajustarStock(item.getLibro().getIsbn(), -item.getCantidad());
            }
        }
    }

    // Calcula el total final a pagar aplicando el descuento del cliente autenticado
    @Override
    public double calcularTotalFinal() {
        Cliente logueado = obtenerUsuarioLogueado();
        Carrito carrito = obtenerCarritoActual();
        if (carrito == null || carrito.estaVacio()) {
            return 0.0;
        }
        return carrito.calcularTotalFinal(logueado);
    }

    // Consulta el historial de compras del usuario autenticado con respaldo en JSON
    @Override
    public List<Compra> obtenerHistorialCompras() {
        Cliente logueado = obtenerUsuarioLogueado();
        if (logueado == null || esVacio(logueado.getId())) {
            return new ArrayList<>();
        }

        List<Compra> compras = repoCompraMySQL != null
                ? repoCompraMySQL.obtenerComprasPorCliente(logueado.getId())
                : null;
        if (compras == null || compras.isEmpty()) {
            List<Compra> respaldoJson = repoCompraJson != null
                    ? repoCompraJson.obtenerComprasPorCliente(logueado.getId())
                    : null;
            if (respaldoJson != null) {
                return respaldoJson;
            }
        }
        return compras != null ? compras : new ArrayList<>();
    }

    // Persiste el estado actual del carrito en MySQL y JSON
    private void sincronizarCarrito(Carrito carrito) {
        Cliente logueado = obtenerUsuarioLogueado();
        if (logueado == null || carrito == null) {
            return;
        }
        if (repoCarritoMySQL != null) {
            repoCarritoMySQL.guardarCarrito(carrito);
        }
        if (repoCarritoJson != null) {
            repoCarritoJson.guardarCarrito(carrito);
        }
    }

    // Registra en el archivo TXT un resumen de la compra realizada
    private void registrarOperacionCompra(Cliente cliente, Carrito carrito, String metodoPago) {
        if (registradorTxt != null) {
            registradorTxt.registrar(
                    "COMPRA | Cliente: " + cliente.getId()
                    + " | Metodo: " + metodoPago
                    + " | Total: " + String.format("%.2f", carrito.calcularTotalFinal(cliente)));
        }
    }

    // Obtiene la versión más reciente del libro asociado a un ítem
    private Libro buscarLibroActualizado(ItemCarrito item) {
        if (item == null || item.getLibro() == null || gestionLibros == null) {
            return null;
        }
        return gestionLibros.buscarLibroPorIsbn(item.getLibro().getIsbn());
    }

    private Cliente obtenerUsuarioLogueado() {
        return gestionUsuarios != null ? gestionUsuarios.getUsuarioLogueado() : null;
    }

    private boolean esVacio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }
}