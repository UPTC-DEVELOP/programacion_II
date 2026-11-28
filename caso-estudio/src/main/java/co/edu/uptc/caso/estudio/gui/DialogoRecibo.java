package co.edu.uptc.caso.estudio.gui;

import co.edu.uptc.caso.estudio.model.Compra;
import co.edu.uptc.caso.estudio.model.ItemCarrito;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

// Diálogo modal encargado de formatear y mostrar el recibo de compra
public class DialogoRecibo extends JDialog {

    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public DialogoRecibo(JFrame parent, Compra compra) {
        super(parent, "Recibo de Compra - Compra Exitosa", true);
        setSize(540, 380);
        setLocationRelativeTo(parent);
        initComponentes(compra);
    }

    private void initComponentes(Compra compra) {
        setLayout(new BorderLayout(10, 10));
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextArea areaRecibo = new JTextArea(construirTextoRecibo(compra));
        areaRecibo.setEditable(false);
        areaRecibo.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JScrollPane scroll = new JScrollPane(areaRecibo);
        scroll.setPreferredSize(new Dimension(500, 280));
        add(scroll, BorderLayout.CENTER);

        JButton btnCerrar = new JButton("Aceptar");
        btnCerrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBoton.add(btnCerrar);
        add(panelBoton, BorderLayout.SOUTH);
    }

    private String construirTextoRecibo(Compra compra) {
        if (compra == null) {
            return "¡Gracias por tu compra! Se ha procesado tu orden con éxito.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("==================================================\n");
        sb.append("           RECIBO DE COMPRA - TIENDA VIRTUAL      \n");
        sb.append("==================================================\n");

        String fechaTexto = compra.getFecha() != null
                ? compra.getFecha().format(FORMATO_FECHA)
                : "N/A";
        sb.append("Fecha: ").append(fechaTexto).append("\n");
        sb.append("Cliente: ").append(valorSeguro(compra.getNombreCliente()))
          .append(" (ID: ").append(valorSeguro(compra.getClienteId())).append(")\n");
        sb.append("Método de pago: ").append(valorSeguro(compra.getMetodoPago())).append("\n");
        sb.append("--------------------------------------------------\n");
        sb.append("DETALLE DE LIBROS:\n");

        List<ItemCarrito> items = compra.getItems();
        if (items == null || items.isEmpty()) {
            sb.append("  (Sin ítems registrados)\n");
        } else {
            for (int i = 0; i < items.size(); i++) {
                ItemCarrito item = items.get(i);
                if (item == null || item.getLibro() == null) {
                    continue;
                }
                double unitario = item.getPrecioUnitarioOverride() != null
                        ? item.getPrecioUnitarioOverride()
                        : item.getLibro().getPrecioVenta();
                sb.append("  - ").append(valorSeguro(item.getLibro().getTitulo()))
                  .append(" | Cant: ").append(item.getCantidad())
                  .append(" | Unit: $").append(String.format("%.2f", unitario))
                  .append(" | Subt: $").append(String.format("%.2f", item.getSubtotalConIva()))
                  .append("\n");
            }
        }

        sb.append("--------------------------------------------------\n");
        sb.append("Subtotal (sin IVA): $").append(String.format("%.2f", compra.getSubtotalSinIva())).append("\n");
        sb.append("Total Impuestos (IVA): $").append(String.format("%.2f", compra.getTotalImpuestos())).append("\n");
        sb.append("Descuento aplicado: -$").append(String.format("%.2f", compra.getDescuentoAplicado())).append("\n");
        sb.append("TOTAL PAGADO: $").append(String.format("%.2f", compra.getTotalPagado())).append("\n");
        sb.append("==================================================\n");
        sb.append("¡Gracias por tu compra!");
        return sb.toString();
    }

    private String valorSeguro(String texto) {
        return texto != null ? texto : "N/A";
    }
}