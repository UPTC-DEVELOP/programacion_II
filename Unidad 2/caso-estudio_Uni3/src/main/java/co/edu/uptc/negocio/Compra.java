package co.edu.uptc.negocio;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Compra {

    private final long id;
    private final LocalDateTime fecha;
    private final String correoCliente;
    private final String nombreCliente;
    private final TipoCliente tipoCliente;
    private final MetodoPago metodoPago;
    private final List<DetalleCompra> detalles;
    private final double subtotal;
    private final double impuestos;
    private final double descuentoPremium;
    private final double total;

    public Compra(long id, LocalDateTime fecha, String correoCliente, String nombreCliente,
                  TipoCliente tipoCliente, MetodoPago metodoPago, List<DetalleCompra> detalles,
                  double subtotal, double impuestos, double descuentoPremium, double total) {
        this.id = id;
        this.fecha = fecha;
        this.correoCliente = correoCliente;
        this.nombreCliente = nombreCliente;
        this.tipoCliente = tipoCliente;
        this.metodoPago = metodoPago;
        this.detalles = new ArrayList<>(detalles);
        this.subtotal = subtotal;
        this.impuestos = impuestos;
        this.descuentoPremium = descuentoPremium;
        this.total = total;
    }

    public long getId() { return id; }
    public LocalDateTime getFecha() { return fecha; }
    public String getCorreoCliente() { return correoCliente; }
    public String getNombreCliente() { return nombreCliente; }
    public TipoCliente getTipoCliente() { return tipoCliente; }
    public MetodoPago getMetodoPago() { return metodoPago; }
    public List<DetalleCompra> getDetalles() { return Collections.unmodifiableList(detalles); }
    public double getSubtotal() { return subtotal; }
    public double getImpuestos() { return impuestos; }
    public double getDescuentoPremium() { return descuentoPremium; }
    public double getTotal() { return total; }
}
