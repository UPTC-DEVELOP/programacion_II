package co.edu.uptc.caso.estudio.negocio;

import co.edu.uptc.caso.estudio.model.Carrito;
import co.edu.uptc.caso.estudio.model.Cliente;
import co.edu.uptc.caso.estudio.model.Compra;
import co.edu.uptc.caso.estudio.model.DatosTransferencia;
import co.edu.uptc.caso.estudio.model.ItemCarrito;
import co.edu.uptc.caso.estudio.model.Libro;
import co.edu.uptc.caso.estudio.negocio.dto.PagoDto;
import co.edu.uptc.caso.estudio.persistence.IRepositorioCarrito;
import co.edu.uptc.caso.estudio.persistence.IRepositorioCompra;
import co.edu.uptc.caso.estudio.persistence.IRegistradorOperaciones;
import java.util.ArrayList;
import java.util.List;

// Coordina el checkout, validación de pago, sincronización de stock e historial de compras
public class GestionComprasImpl implements IGestionCompras {

    private final IRepositorioCompra repoCompraMySQL;
    private final IRepositorioCompra repoCompraJson;
    private final IGestionUsuarios gestionUsuarios;
    private final IGestionLibros gestionLibros;
    private final IRegistradorOperaciones registradorOperaciones;
    private final GestorCarrito gestorCarrito;
    private final ValidadorPago validadorPago;
    private DatosTransferencia datosTransferencia;
    private String descripcionMetodoPago;

    public GestionComprasImpl(
            IRepositorioCarrito repoCarritoMySQL,
            IRepositorioCarrito repoCarritoJson,
            IRepositorioCompra repoCompraMySQL,
            IRepositorioCompra repoCompraJson,
            IGestionUsuarios gestionUsuarios,
            IGestionLibros gestionLibros,
            IRegistradorOperaciones registradorOperaciones) {
        this.repoCompraMySQL = repoCompraMySQL;
        this.repoCompraJson = repoCompraJson;
        this.gestionUsuarios = gestionUsuarios;
        this.gestionLibros = gestionLibros;
        this.registradorOperaciones = registradorOperaciones;
        this.gestorCarrito = new GestorCarrito(
                repoCarritoMySQL, repoCarritoJson, gestionUsuarios, gestionLibros);
        this.validadorPago = new ValidadorPago();
    }

    @Override
    public Carrito obtenerCarritoActual() {
        return gestorCarrito.obtenerCarritoActual();
    }

    @Override
    public void agregarAlCarrito(Libro libro, int cantidad) {
        gestorCarrito.agregarLibro(libro, cantidad);
    }

    @Override
    public String actualizarCantidadCarrito(String isbn, int nuevaCantidad) {
        return gestorCarrito.actualizarCantidad(isbn, nuevaCantidad);
    }

    @Override
    public boolean eliminarDelCarrito(List<String> isbns) {
        return gestorCarrito.eliminarLibros(isbns);
    }

    @Override
    public void vaciarCarrito() {
        gestorCarrito.vaciarCarrito();
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
        this.descripcionMetodoPago = null;

        String error = validadorPago.validar(pago);
        if (error != null) {
            return error;
        }

        this.datosTransferencia = validadorPago.construirDatosTransferencia(pago);
        this.descripcionMetodoPago = construirDescripcionMetodoPago(pago, this.datosTransferencia);
        return null;
    }

    @Override
    public boolean finalizarCompra() {
        Cliente logueado = obtenerUsuarioLogueado();
        if (logueado == null) {
            return false;
        }

        Carrito c = obtenerCarritoActual();
        if (c == null || c.estaVacio() || datosTransferencia == null || !tieneStockSuficiente(c)) {
            return false;
        }

        String infoPago = resolverInfoPago();
        double totalPagado = c.calcularTotalFinal(logueado);

        boolean compraMySQL = repoCompraMySQL != null
                && repoCompraMySQL.registrarCompra(logueado, c, infoPago);
        boolean compraJson = repoCompraJson != null
                && repoCompraJson.registrarCompra(logueado, c, infoPago);

        this.datosTransferencia = null;
        this.descripcionMetodoPago = null;

        if (!compraMySQL && !compraJson) {
            return false;
        }

        sincronizarStockTrasCompra(c, compraMySQL);
        vaciarCarrito();
        registrarOperacionCompra(logueado, infoPago, totalPagado);
        return true;
    }

    @Override
    public double calcularTotalFinal() {
        Cliente logueado = obtenerUsuarioLogueado();
        Carrito carrito = obtenerCarritoActual();
        if (carrito == null || carrito.estaVacio()) {
            return 0.0;
        }
        return carrito.calcularTotalFinal(logueado);
    }

    @Override
    public List<Compra> obtenerHistorialCompras() {
        Cliente logueado = obtenerUsuarioLogueado();
        if (logueado == null || logueado.getId() == null || logueado.getId().trim().isEmpty()) {
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

    private String construirDescripcionMetodoPago(PagoDto pago, DatosTransferencia transferencia) {
        String metodo = (pago != null && pago.getMetodo() != null) ? pago.getMetodo().trim() : "";
        String bancoDto = (pago != null && pago.getBanco() != null) ? pago.getBanco().trim() : "";
        String bancoTransf = (transferencia != null && transferencia.getBanco() != null)
                ? transferencia.getBanco().trim()
                : "";
        String detalle = (transferencia != null && transferencia.esNequi()) || bancoDto.isEmpty()
                ? bancoTransf
                : bancoDto;

        if (!metodo.isEmpty() && !detalle.isEmpty()) {
            return metodo + " (" + detalle + ")";
        }
        if (!metodo.isEmpty()) {
            return metodo;
        }
        if (!detalle.isEmpty()) {
            return detalle;
        }
        return "No especificado";
    }

    private String resolverInfoPago() {
        if (this.descripcionMetodoPago != null && !this.descripcionMetodoPago.trim().isEmpty()) {
            return this.descripcionMetodoPago.trim();
        }
        if (this.datosTransferencia != null && this.datosTransferencia.getBanco() != null
                && !this.datosTransferencia.getBanco().trim().isEmpty()) {
            return this.datosTransferencia.getBanco().trim();
        }
        return "No especificado";
    }

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

    private void sincronizarStockTrasCompra(Carrito carrito, boolean descontadoEnMySQL) {
        if (gestionLibros == null || carrito == null || carrito.getItems() == null) {
            return;
        }
        for (ItemCarrito item : carrito.getItems()) {
            if (item != null && item.getLibro() != null && item.getCantidad() > 0) {
                String isbn = item.getLibro().getIsbn();
                if (descontadoEnMySQL) {
                    gestionLibros.sincronizarStockRespaldo(isbn);
                } else {
                    gestionLibros.ajustarStock(isbn, -item.getCantidad());
                }
            }
        }
    }

    private void registrarOperacionCompra(Cliente cliente, String metodoPago, double totalPagado) {
        if (registradorOperaciones != null) {
            registradorOperaciones.registrar(
                    "COMPRA | Cliente: " + cliente.getId()
                    + " | Metodo: " + metodoPago
                    + " | Total: " + String.format("%.2f", totalPagado));
        }
    }

    private Cliente obtenerUsuarioLogueado() {
        return gestionUsuarios != null ? gestionUsuarios.getUsuarioLogueado() : null;
    }
}