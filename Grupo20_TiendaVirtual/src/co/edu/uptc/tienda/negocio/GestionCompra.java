package co.edu.uptc.tienda.negocio;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import co.edu.uptc.tienda.excepcion.CompraExcepcion;
import co.edu.uptc.tienda.interfaz.IGestionCompra;
import co.edu.uptc.tienda.interfaz.IPersistenciaCompra;
import co.edu.uptc.tienda.interfaz.IPersistenciaRecibo;
import co.edu.uptc.tienda.interfaz.IRegistroOperaciones;
import co.edu.uptc.tienda.modelo.Cliente;
import co.edu.uptc.tienda.modelo.Compra;
import co.edu.uptc.tienda.modelo.DetalleCompra;
import co.edu.uptc.tienda.modelo.ItemCarrito;
import co.edu.uptc.tienda.persistencia.CompraBD;
import co.edu.uptc.tienda.persistencia.CompraJson;
import co.edu.uptc.tienda.persistencia.ReciboTxt;
import co.edu.uptc.tienda.persistencia.RegistroOperacionesTxt;

public class GestionCompra implements IGestionCompra {

    public static final int COMPRAS_CLIENTE_FRECUENTE = 3;
    public static final double DESCUENTO_CLIENTE_FRECUENTE = 5;

    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final List<IPersistenciaCompra> persistencias;
    private final IPersistenciaRecibo recibos;
    private final IRegistroOperaciones registro;
    private String avisoGuardado;

    public GestionCompra() {
        this(crearPersistencias(), new ReciboTxt("datos/recibos"), new RegistroOperacionesTxt("datos/operaciones.txt"));
    }

    public GestionCompra(List<IPersistenciaCompra> persistencias, IPersistenciaRecibo recibos,
            IRegistroOperaciones registro) {
        this.persistencias = persistencias;
        this.recibos = recibos;
        this.registro = registro;
    }

    private static List<IPersistenciaCompra> crearPersistencias() {
        List<IPersistenciaCompra> persistencias = new ArrayList<>();
        persistencias.add(new CompraBD());
        persistencias.add(new CompraJson("datos/compras.json"));
        return persistencias;
    }

    @Override
    public Compra registrarCompra(Cliente cliente, String metodoPago, double porcentajeDescuento,
            List<ItemCarrito> items) throws CompraExcepcion {
        if (cliente == null) {
            throw new CompraExcepcion("La compra necesita un cliente.");
        }
        if (metodoPago == null || metodoPago.trim().isEmpty()) {
            throw new CompraExcepcion("Seleccione un método de pago.");
        }
        if (items == null || items.isEmpty()) {
            throw new CompraExcepcion("La compra no tiene libros.");
        }
        List<DetalleCompra> detalles = new ArrayList<>();
        for (ItemCarrito item : items) {
            detalles.add(new DetalleCompra(item.getLibro().getIsbn(), item.getLibro().getTitulo(), item.getCantidad(),
                    item.getLibro().getPrecio(), item.getLibro().getIvaPorcentaje()));
        }
        Compra compra = new Compra(siguienteNumero(), cliente.getId(), cliente.getNombreCompleto(),
                LocalDateTime.now().format(FORMATO), metodoPago, porcentajeDescuento, detalles);
        List<String> errores = new ArrayList<>();
        for (IPersistenciaCompra persistencia : persistencias) {
            try {
                persistencia.guardar(compra);
            } catch (CompraExcepcion e) {
                errores.add(e.getMessage());
            }
        }
        if (errores.size() == persistencias.size()) {
            throw new CompraExcepcion("La compra no se pudo guardar en ninguna parte:\n" + String.join("\n", errores));
        }
        try {
            recibos.guardar(compra.getNumero(), generarRecibo(compra));
        } catch (CompraExcepcion e) {
            errores.add(e.getMessage());
        }
        registro.registrar("Compras", "Finalizar compra", "Compra " + compra.getNumero() + ", cliente "
                + cliente.getId() + ", " + metodoPago + ", total " + formatear(compra.getTotal()));
        avisoGuardado = errores.isEmpty() ? null
                : "La compra se guardó, pero no en todas partes:\n" + String.join("\n", errores);
        return compra;
    }

    @Override
    public String getAvisoGuardado() {
        return avisoGuardado;
    }

    private int siguienteNumero() throws CompraExcepcion {
        int mayor = 0;
        for (Compra compra : listarCompras()) {
            mayor = Math.max(mayor, compra.getNumero());
        }
        return mayor + 1;
    }

    @Override
    public List<Compra> listarCompras() throws CompraExcepcion {
        List<Compra> compras = new ArrayList<>();
        List<String> errores = new ArrayList<>();
        for (IPersistenciaCompra persistencia : persistencias) {
            try {
                for (Compra compra : persistencia.leer()) {
                    if (buscar(compras, compra.getNumero()) == null) {
                        compras.add(compra);
                    }
                }
            } catch (CompraExcepcion e) {
                errores.add(e.getMessage());
            }
        }
        if (!persistencias.isEmpty() && errores.size() == persistencias.size()) {
            throw new CompraExcepcion("No se pudieron leer las compras:\n" + String.join("\n", errores));
        }
        compras.sort(Comparator.comparingInt(Compra::getNumero));
        return compras;
    }

    private Compra buscar(List<Compra> compras, int numero) {
        for (Compra compra : compras) {
            if (compra.getNumero() == numero) {
                return compra;
            }
        }
        return null;
    }

    @Override
    public List<Compra> listarComprasCliente(String idCliente) throws CompraExcepcion {
        List<Compra> delCliente = new ArrayList<>();
        for (Compra compra : listarCompras()) {
            if (compra.getIdCliente().equals(idCliente)) {
                delCliente.add(compra);
            }
        }
        return delCliente;
    }

    @Override
    public boolean tieneVentas(String isbn) throws CompraExcepcion {
        for (Compra compra : listarCompras()) {
            if (compra.tieneLibro(isbn)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean clienteComproLibro(String idCliente, String isbn) throws CompraExcepcion {
        for (Compra compra : listarComprasCliente(idCliente)) {
            if (compra.tieneLibro(isbn)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int contarCompras(String idCliente) throws CompraExcepcion {
        return listarComprasCliente(idCliente).size();
    }

    @Override
    public double obtenerDescuentoFrecuente(String idCliente) {
        if (idCliente == null) {
            return 0;
        }
        try {
            return contarCompras(idCliente) >= COMPRAS_CLIENTE_FRECUENTE ? DESCUENTO_CLIENTE_FRECUENTE : 0;
        } catch (CompraExcepcion e) {
            return 0;
        }
    }

    @Override
    public String generarRecibo(Compra compra) {
        StringBuilder recibo = new StringBuilder();
        String linea = "-".repeat(64) + "\n";
        recibo.append("TIENDA VIRTUAL DE LIBROS\n");
        recibo.append("Recibo de compra N.º ").append(compra.getNumero()).append("\n");
        recibo.append("Fecha: ").append(compra.getFecha()).append("\n");
        recibo.append("Cliente: ").append(compra.getNombreCliente()).append(" (").append(compra.getIdCliente())
                .append(")\n");
        recibo.append("Método de pago: ").append(compra.getMetodoPago()).append(" (pago simulado, aprobado)\n");
        recibo.append(linea);
        recibo.append(String.format("%-34s %5s %6s %15s%n", "Libro", "IVA", "Cant.", "Subtotal"));
        for (DetalleCompra detalle : compra.getDetalles()) {
            recibo.append(String.format("%-34s %4.0f%% %6d %15s%n", recortar(detalle.getTitulo(), 34),
                    detalle.getIvaPorcentaje(), detalle.getCantidad(), formatear(detalle.obtenerSubtotal())));
        }
        recibo.append(linea);
        recibo.append(String.format("%-48s %15s%n", "Subtotal:", formatear(compra.getSubtotal())));
        recibo.append(String.format("%-48s %15s%n", "Impuestos (IVA):", formatear(compra.getImpuestos())));
        recibo.append(String.format("%-48s %15s%n",
                "Descuento (" + String.format("%.0f", compra.getPorcentajeDescuento()) + " %):",
                "-" + formatear(compra.getDescuento())));
        recibo.append(String.format("%-48s %15s%n", "Total pagado:", formatear(compra.getTotal())));
        return recibo.toString();
    }

    private String recortar(String texto, int largo) {
        return texto.length() <= largo ? texto : texto.substring(0, largo - 3) + "...";
    }

    private String formatear(double valor) {
        return String.format("$%,.2f", valor);
    }
}
