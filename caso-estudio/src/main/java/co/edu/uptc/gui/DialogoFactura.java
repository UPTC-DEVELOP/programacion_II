package co.edu.uptc.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import co.edu.uptc.modelo.dto.CompraDtos.FacturaDto;
import co.edu.uptc.modelo.dto.CompraDtos.LineaFacturaDto;

/**
 * IMPLEMENTA IDialogoFactura Y MUESTRA LA FACTURA DE UNA COMPRA
 *
 * @author: Equipo de desarrollo grupo 7 UPTC
 * @modificacion:Brayan Panqueva
 */
public class DialogoFactura extends JDialog {

    private static final long serialVersionUID = 1L;

    public DialogoFactura(java.awt.Frame padre, FacturaDto factura) {
        this(padre, factura, f -> {
        });
    }

    /*modificacion:Brayan Panqueva*/
    public DialogoFactura(java.awt.Frame padre, FacturaDto factura, Consumer<FacturaDto> guardarFactura) {
        super(padre, "Factura de compra #" + factura.idVenta(), true);
        setLayout(new BorderLayout(10, 10));
        setSize(650, 500);
        setLocationRelativeTo(padre);
        JTextArea texto = new JTextArea();
        texto.setEditable(false);
        texto.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 12));
        NumberFormat moneda = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));
        StringBuilder sb = new StringBuilder();
        sb.append("FACTURA DE COMPRA #").append(factura.idVenta()).append("\n");
        sb.append("Fecha: ").append(factura.fecha()).append("\n");
        sb.append("Cliente: ").append(factura.clienteNombre()).append("\n");
        sb.append("Correo: ").append(factura.clienteCorreo()).append("\n");
        sb.append("Tipo: ").append(factura.tipoCliente()).append("\n\n");
        sb.append("ISBN                 TÍTULO                         CANT.   P.UNIT       SUBTOTAL\n");
        sb.append("-------------------------------------------------------------------------------\n");
        for (LineaFacturaDto l : factura.lineas()) {
            sb.append(String.format("%-20s %-30s %4d %12s %12s%n", l.isbn(), recortar(l.titulo(), 30), l.cantidad(), moneda.format(l.precioUnitario()), moneda.format(l.subtotal())));
        }
        sb.append("\nSubtotal: ").append(moneda.format(factura.subtotal()));
        sb.append("\nIVA: ").append(moneda.format(factura.impuestos()));
        sb.append("\nDescuento: ").append(moneda.format(factura.descuento()));
        sb.append("\nTOTAL PAGADO: ").append(moneda.format(factura.total()));
        texto.setText(sb.toString());
        add(new JScrollPane(texto), BorderLayout.CENTER);
        JPanel sur = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton cerrar = new JButton("Aceptar");
        cerrar.addActionListener(e -> {
            guardarFactura.accept(factura);
            dispose();
        });
        sur.add(cerrar);
        add(sur, BorderLayout.SOUTH);
    }

    private String recortar(String valor, int max) {
        if (valor == null) {
            return "";

        }
        return valor.length() <= max ? valor : valor.substring(0, max - 1) + "…";
    }
}
