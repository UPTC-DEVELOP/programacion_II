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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GestionComprasImpl implements IGestionCompras {

    private static final String ID_CLIENTE_ANONIMO = "anonimo";
    private static final String REGEX_TARJETA = "\\d{16}";
    private static final String REGEX_CVV = "\\d{3,4}";
    private static final String REGEX_EXPIRACION = "(0[1-9]|1[0-2])/\\d{2}";
    private static final String REGEX_NEQUI = "3\\d{9}";

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

    @Override
    public Carrito obtenerCarritoActual() {
        Cliente logueado = gestionUsuarios.getUsuarioLogueado();
        if (logueado == null) {
            return new Carrito(ID_CLIENTE_ANONIMO);
        }

        Carrito carrito = repoCarritoMySQL.obtenerCarritoPorCliente(logueado.getId());
        if (carrito == null || carrito.estaVacio()) {
            Carrito respaldoJson = repoCarritoJson.obtenerCarritoPorCliente(logueado.getId());
            if (respaldoJson != null && !respaldoJson.estaVacio()) {
                carrito = respaldoJson;
            }
        }
        if (carrito == null) {
            carrito = new Carrito(logueado.getId());
        }
        refrescarLibrosDelCarrito(carrito);
        return carrito;
    }

    private void refrescarLibrosDelCarrito(Carrito carrito) {
        if (carrito == null || carrito.getItems() == null) {
            return;
        }
        Iterator<ItemCarrito> it = carrito.getItems().iterator();
        while (it.hasNext()) {
            ItemCarrito item = it.next();
            Libro actual = (item != null && item.getLibro() != null)
                    ? gestionLibros.buscarLibroPorIsbn(item.getLibro().getIsbn())
                    : null;
            if (actual == null) {
                it.remove();
            } else {
                item.setLibro(actual);
            }
        }
    }

    @Override
    public void agregarAlCarrito(Libro libro, int cantidad) {
        if (gestionUsuarios.getUsuarioLogueado() == null || libro == null || cantidad <= 0) {
            return;
        }

        Carrito c = obtenerCarritoActual();
        ItemCarrito existente = c.buscarItem(libro.getIsbn());
        int cantidadActual = (existente != null) ? existente.getCantidad() : 0;

        Libro libroActualizado = gestionLibros.buscarLibroPorIsbn(libro.getIsbn());
        int stockDisponible = (libroActualizado != null) ? libroActualizado.getStock() : libro.getStock();

        if (cantidadActual + cantidad > stockDisponible) {
            return;
        }

        c.agregarItem(libro, cantidad);
        sincronizarCarrito(c);
    }

    @Override
    public String actualizarCantidadCarrito(String isbn, int nuevaCantidad) {
        if (gestionUsuarios.getUsuarioLogueado() == null) {
            return "Debe iniciar sesión primero.";
        }
        if (nuevaCantidad <= 0) {
            return "La cantidad debe ser mayor a 0.";
        }

        Carrito c = obtenerCarritoActual();
        ItemCarrito item = c.buscarItem(isbn);
        if (item == null) {
            return "El libro no se encuentra en el carrito.";
        }

        Libro l = gestionLibros.buscarLibroPorIsbn(isbn);
        if (l != null && nuevaCantidad > l.getStock()) {
            return "Stock insuficiente. Disponible: " + l.getStock();
        }

        c.actualizarCantidad(isbn, nuevaCantidad);
        sincronizarCarrito(c);
        return null;
    }

    @Override
    public boolean eliminarDelCarrito(List<String> isbns) {
        if (gestionUsuarios.getUsuarioLogueado() == null || isbns == null || isbns.isEmpty()) {
            return false;
        }

        Carrito c = obtenerCarritoActual();
        boolean eliminado = c.eliminarItems(isbns);
        if (eliminado) {
            sincronizarCarrito(c);
        }
        return eliminado;
    }

    @Override
    public void vaciarCarrito() {
        Cliente logueado = gestionUsuarios.getUsuarioLogueado();
        if (logueado != null) {
            repoCarritoMySQL.vaciarCarrito(logueado.getId());
            repoCarritoJson.vaciarCarrito(logueado.getId());
        }
    }

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

    private String validarPagoTarjeta(PagoDto pago) {
        if (pago.getBanco() == null || pago.getBanco().trim().isEmpty()) {
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

        YearMonth vencimiento = YearMonth.parse(pago.getExpiracion(), DateTimeFormatter.ofPattern("MM/yy"));
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

    @Override
    public boolean finalizarCompra() {
        Cliente logueado = gestionUsuarios.getUsuarioLogueado();
        if (logueado == null) {
            return false;
        }

        Carrito c = obtenerCarritoActual();
        if (c == null || c.estaVacio() || datosTransferencia == null) {
            return false;
        }

        String infoPago = datosTransferencia.getBanco();
        boolean compraMySQL = repoCompraMySQL.registrarCompra(logueado, c, infoPago);
        boolean compraJson = repoCompraJson.registrarCompra(logueado, c, infoPago);

        this.datosTransferencia = null;
        if (!compraMySQL && !compraJson) {
            return false;
        }

        registrarOperacionCompra(logueado, c, infoPago);
        return true;
    }

    @Override
    public double calcularTotalFinal() {
        Cliente logueado = gestionUsuarios.getUsuarioLogueado();
        Carrito carrito = obtenerCarritoActual();
        if (carrito == null || carrito.estaVacio()) {
            return 0.0;
        }
        return carrito.calcularTotalFinal(logueado);
    }

    public List<Compra> obtenerHistorialCompras() {
        Cliente logueado = gestionUsuarios.getUsuarioLogueado();
        if (logueado == null || logueado.getId() == null) {
            return new ArrayList<>();
        }

        List<Compra> compras = repoCompraMySQL.obtenerComprasPorCliente(logueado.getId());
        if (compras == null || compras.isEmpty()) {
            List<Compra> respaldoJson = repoCompraJson.obtenerComprasPorCliente(logueado.getId());
            if (respaldoJson != null) {
                return respaldoJson;
            }
        }
        return compras != null ? compras : new ArrayList<>();
    }

    private void sincronizarCarrito(Carrito carrito) {
        Cliente logueado = gestionUsuarios.getUsuarioLogueado();
        if (logueado == null || carrito == null) {
            return;
        }
        repoCarritoMySQL.guardarCarrito(carrito);
        repoCarritoJson.guardarCarrito(carrito);
    }

    private void registrarOperacionCompra(Cliente cliente, Carrito carrito, String metodoPago) {
        if (registradorTxt != null) {
            registradorTxt.registrar(
                    "COMPRA | Cliente: " + cliente.getId()
                    + " | Metodo: " + metodoPago
                    + " | Total: " + String.format("%.2f", carrito.calcularTotalFinal(cliente)));
        }
    }
}