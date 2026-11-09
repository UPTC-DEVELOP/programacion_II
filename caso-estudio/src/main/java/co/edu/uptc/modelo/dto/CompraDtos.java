package co.edu.uptc.modelo.dto;

import java.util.List;
/*
 * DTO QUE CONTIENE LOS DATOS DE LA COMPRA Y FACTURA PARA SER UTILIZADOS EN EL SISTEMA.
 * @author: Equipo de desarrollo grupo 7 UPTC
 * */
/*modificacion:Brayan Panqueva*/
public final class CompraDtos {
    private CompraDtos() { }

    public record ItemCompraDto(String isbn, int cantidad, double precioUnitario, double subtotal) { }

    public record PagoDto(String metodo, String numeroTarjeta, String contrasena,
                          double montoPagado, double cambio) { }

    public record SolicitudCompraDto(int idCliente, double subtotal, double impuestos,
                                     double descuento, double total, String metodoPago,
                                     List<ItemCompraDto> items) { }

    public record LineaFacturaDto(String isbn, String titulo, int cantidad,
                                  double precioUnitario, double subtotal, int porcentajeIva) { }

    public record FacturaDto(int idVenta, String fecha, String clienteNombre,
                             String clienteCorreo, String tipoCliente,
                             double subtotal, double impuestos, double descuento,
                             double total, List<LineaFacturaDto> lineas) { }
}
