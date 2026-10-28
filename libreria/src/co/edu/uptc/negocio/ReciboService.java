package co.edu.uptc.negocio;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class ReciboService {

    private final Path carpeta;

    public ReciboService(Path carpeta) {
        this.carpeta = carpeta;
        try {
            Files.createDirectories(carpeta);
        } catch (IOException e) {
            throw new PersistenciaException("No se pudo crear la carpeta de recibos.", e);
        }
    }

    public String generar(Compra compra) {
        String contenido = construirContenido(compra);
        Path archivo = carpeta.resolve("recibo-" + compra.getId() + ".txt");
        try {
            Files.write(archivo, contenido.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new PersistenciaException("La compra fue registrada, pero no se pudo generar el recibo.", e);
        }
        return contenido;
    }

    public String getRutaArchivo(Compra compra) {
        return carpeta.resolve("recibo-" + compra.getId() + ".txt").toString();
    }

    private String construirContenido(Compra compra) {
        DecimalFormat formato = new DecimalFormat("$ #,##0.00",
                DecimalFormatSymbols.getInstance(Locale.forLanguageTag("es-CO")));
        StringBuilder sb = new StringBuilder();
        sb.append("TIENDA VIRTUAL DE LIBROS - UPTC\n");
        sb.append("=============================================\n");
        sb.append("Recibo de compra #").append(compra.getId()).append("\n");
        sb.append("Fecha: ").append(compra.getFecha()).append("\n");
        sb.append("Cliente: ").append(compra.getNombreCliente()).append("\n");
        sb.append("Correo: ").append(compra.getCorreoCliente()).append("\n");
        sb.append("Tipo: ").append(compra.getTipoCliente().getEtiqueta()).append("\n");
        sb.append("Pago: ").append(compra.getMetodoPago().getEtiqueta()).append("\n\n");
        for (DetalleCompra d : compra.getDetalles()) {
            sb.append(d.getTitulo()).append(" | ")
                    .append(d.getCantidad()).append(" x ")
                    .append(formato.format(d.getPrecioUnitarioSinIva())).append("\n")
                    .append("  Subtotal: ").append(formato.format(d.getSubtotal()))
                    .append(" | IVA: ").append(formato.format(d.getImpuesto()))
                    .append(" | Total: ").append(formato.format(d.getTotal())).append("\n");
        }
        sb.append("\nSubtotal: ").append(formato.format(compra.getSubtotal())).append("\n");
        sb.append("Impuestos: ").append(formato.format(compra.getImpuestos())).append("\n");
        sb.append("Descuento Premium: ").append(formato.format(compra.getDescuentoPremium())).append("\n");
        sb.append("TOTAL A PAGAR: ").append(formato.format(compra.getTotal())).append("\n");
        sb.append("=============================================\n");
        return sb.toString();
    }
}
