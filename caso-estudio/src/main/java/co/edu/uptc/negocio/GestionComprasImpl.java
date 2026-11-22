package co.edu.uptc.negocio;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import co.edu.uptc.model.Carrito;
import co.edu.uptc.model.Cliente;
import co.edu.uptc.model.DatosTransferencia;
import co.edu.uptc.model.ItemCarrito;
import co.edu.uptc.model.Libro;
import co.edu.uptc.model.enums.MetodoPago;
import co.edu.uptc.negocio.dto.PagoDto;
import co.edu.uptc.persistence.IRepositorioCarrito;
import co.edu.uptc.persistence.IRepositorioCompra;

public class GestionComprasImpl implements IGestionCompras {

    private final IRepositorioCarrito repoCarritoMySQL;
    private final IRepositorioCarrito repoCarritoJson;
    private final IRepositorioCompra repoCompra;
    private final IGestionUsuarios gestionUsuarios;
    private final IGestionLibros gestionLibros;
    private DatosTransferencia datosTransferencia;

    public GestionComprasImpl(IRepositorioCarrito repoCarritoMySQL, IRepositorioCarrito repoCarritoJson,
                              IRepositorioCompra repoCompra, IGestionUsuarios gestionUsuarios,
                              IGestionLibros gestionLibros) {
        this.repoCarritoMySQL = repoCarritoMySQL;
        this.repoCarritoJson = repoCarritoJson;
        this.repoCompra = repoCompra;
        this.gestionUsuarios = gestionUsuarios;
        this.gestionLibros = gestionLibros;
    }

    @Override
    public Carrito obtenerCarritoActual() {
        Cliente logueado = gestionUsuarios.getUsuarioLogueado();
        if (logueado == null) return new Carrito("anonimo");
        return repoCarritoMySQL.obtenerCarritoPorCliente(logueado.getId());
    }

    @Override
    public void agregarAlCarrito(Libro libro, int cantidad) {
        if (gestionUsuarios.getUsuarioLogueado() == null) return;
        Carrito c = obtenerCarritoActual();
        c.agregarItem(libro, cantidad);
        repoCarritoMySQL.guardarCarrito(c);
        repoCarritoJson.guardarCarrito(c);
        gestionLibros.ajustarStock(libro.getIsbn(), -cantidad);
    }

    @Override
    public String actualizarCantidadCarrito(String isbn, int nuevaCantidad) {
        if (gestionUsuarios.getUsuarioLogueado() == null) return "Debe iniciar sesión primero.";
        if (nuevaCantidad <= 0) return "La cantidad debe ser mayor a 0.";

        Carrito c = obtenerCarritoActual();
        ItemCarrito item = c.buscarItem(isbn);
        if (item == null) return "El libro no se encuentra en el carrito.";

        int diferencia = nuevaCantidad - item.getCantidad();
        Libro l = gestionLibros.buscarLibroPorIsbn(isbn);
        if (l != null && diferencia > l.getStock()) {
            return "Stock insuficiente. Disponible: " + l.getStock();
        }

        c.actualizarCantidad(isbn, nuevaCantidad);
        repoCarritoMySQL.guardarCarrito(c);
        repoCarritoJson.guardarCarrito(c);
        gestionLibros.ajustarStock(isbn, -diferencia);
        return null;
    }

    @Override
    public boolean eliminarDelCarrito(List<String> isbns) {
        if (gestionUsuarios.getUsuarioLogueado() == null || isbns == null || isbns.isEmpty()) return false;

        Carrito c = obtenerCarritoActual();
        for (String isbn : isbns) {
            ItemCarrito item = c.buscarItem(isbn);
            if (item != null) gestionLibros.ajustarStock(isbn, item.getCantidad());
        }

        boolean eliminado = c.eliminarItems(isbns);
        if (eliminado) {
            repoCarritoMySQL.guardarCarrito(c);
            repoCarritoJson.guardarCarrito(c);
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
        if (pago == null) return "Datos de pago inválidos.";
        if (MetodoPago.TARJETA.getEtiqueta().equalsIgnoreCase(pago.getMetodo())) {
            if (pago.getBanco() == null || pago.getBanco().isBlank()) return "Seleccione un banco.";
            if (pago.getNumeroTarjeta() == null || !pago.getNumeroTarjeta().matches("\\d{16}")) return "Número de tarjeta inválido (16 dígitos).";
            if (pago.getCvv() == null || !pago.getCvv().matches("\\d{3,4}")) return "CVV inválido (3 o 4 dígitos).";
            if (pago.getExpiracion() == null || !pago.getExpiracion().matches("(0[1-9]|1[0-2])/\\d{2}")) return "Fecha de expiración inválida (MM/AA).";
            this.datosTransferencia = new DatosTransferencia(pago.getBanco(), pago.getNumeroTarjeta(), pago.getCvv(), pago.getExpiracion(), null);
        } else if (MetodoPago.TRANSFERENCIA.getEtiqueta().equalsIgnoreCase(pago.getMetodo())) {
            if (pago.getNumeroNequi() == null || !pago.getNumeroNequi().matches("3\\d{9}")) return "Número de Nequi inválido (10 dígitos empezando por 3).";
            this.datosTransferencia = new DatosTransferencia(DatosTransferencia.NEQUI, null, null, null, pago.getNumeroNequi());
        } else {
            return "Método de pago no válido.";
        }
        return null;
    }

    @Override
    public boolean finalizarCompra() {
        Cliente logueado = gestionUsuarios.getUsuarioLogueado();
        if (logueado == null) return false;
        Carrito c = obtenerCarritoActual();
        if (c.getItems().isEmpty()) return false;

        try (PrintWriter out = new PrintWriter(new FileWriter("data/operaciones_tienda.txt", true))) {
            out.println("COMPRA - Cliente: " + logueado.getNombreCompleto() + " (" + logueado.getCorreo() + ")");
            out.println("Total Pagado: $" + c.calcularTotal());
            out.println("----------------------------------------");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return repoCompra.registrarCompra(logueado.getId(), c);
    }
}
