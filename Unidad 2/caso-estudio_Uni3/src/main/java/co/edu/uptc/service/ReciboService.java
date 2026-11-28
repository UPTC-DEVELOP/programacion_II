package co.edu.uptc.service;

import co.edu.uptc.model.LineaPedido;
import co.edu.uptc.model.Pedido;
import co.edu.uptc.util.FormatoMoneda;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

// ReciboService solo convierte un pedido confirmado en un comprobante legible.
public class ReciboService {
    private final Path carpeta;
    public ReciboService(Path carpeta) { this.carpeta=carpeta; try { Files.createDirectories(carpeta); } catch(IOException e){ throw new RuntimeException("No se pudo crear la carpeta de recibos.",e); } }
    public String generar(Pedido pedido) {
        StringBuilder texto=new StringBuilder(); texto.append("TIENDA VIRTUAL DE LIBROS\n"); texto.append("Pedido #").append(pedido.getId()).append("\nFecha: ").append(pedido.getFecha()).append("\nCliente: ").append(pedido.getCorreoCliente()).append("\nPago: ").append(pedido.getMetodoPago()).append("\n\n");
        for(LineaPedido l:pedido.getLineas()) texto.append(l.getTitulo()).append(" | ").append(l.getCantidad()).append(" x ").append(FormatoMoneda.formatear(l.calcularPrecioSinIva())).append(" | Subtotal: ").append(FormatoMoneda.formatear(l.calcularSubtotal())).append(" | IVA: ").append(FormatoMoneda.formatear(l.calcularImpuesto())).append("\n");
        texto.append("\nSubtotal: ").append(FormatoMoneda.formatear(pedido.getSubtotal())).append("\nImpuestos: ").append(FormatoMoneda.formatear(pedido.getImpuestos())).append("\nDescuento: ").append(FormatoMoneda.formatear(pedido.getDescuento())).append("\nTOTAL: ").append(FormatoMoneda.formatear(pedido.getTotal())).append("\n");
        try { Files.write(carpeta.resolve("recibo_"+pedido.getId()+".txt"), texto.toString().getBytes(StandardCharsets.UTF_8)); } catch(IOException e){ throw new RuntimeException("No se pudo guardar el recibo.",e); } return texto.toString();
    }
}
