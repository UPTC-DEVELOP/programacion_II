package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import model.Cliente;
import model.Compra;
import model.ItemCompra;

public class VentanaRecibo extends JFrame {

    private static final long serialVersionUID = 1L;

    private final Cliente cliente;
    private final Compra compra;
    private final JFrame padre;
    private JButton btnCerrar;

    public VentanaRecibo(Cliente cliente, Compra compra, JFrame padre) {
        this.cliente = cliente;
        this.compra = compra;
        this.padre = padre;

        setTitle("Recibo de compra");
        setSize(520, 460);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(padre);
        setLayout(new BorderLayout(8, 8));

        JTextArea area = new JTextArea(construirRecibo());
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        add(new JScrollPane(area), BorderLayout.CENTER);

        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnCerrar = new JButton("Cerrar");
        panelBoton.add(btnCerrar);
        add(panelBoton, BorderLayout.SOUTH);

        new EventosRecibo(this).registrarEventos(btnCerrar, padre);
    }

    private String construirRecibo() {
        StringBuilder texto = new StringBuilder();
        texto.append("TIENDA VIRTUAL - RECIBO\n\n");
        texto.append("Cliente: ")
                .append(cliente != null ? cliente.getNombreCompleto() : compra.getCorreoCliente())
                .append("\n");
        texto.append("Correo: ")
                .append(cliente != null ? cliente.getCorreo() : compra.getCorreoCliente())
                .append("\n");
        texto.append("Fecha: ").append(compra.getFecha()).append("\n");
        texto.append("Metodo de pago: ").append(compra.getMetodoPago()).append("\n\n");
        texto.append("DETALLE\n");

        if (compra.getItems() != null) {
            for (ItemCompra item : compra.getItems()) {
                if (item == null || item.getLibro() == null) {
                    continue;
                }
                texto.append("- ")
                        .append(item.getLibro().getTitulo())
                        .append(" x").append(item.getCantidad())
                        .append(" = ").append(String.format("$%,.2f", item.getTotalLinea()))
                        .append("\n");
            }
        }

        double subtotal = 0;
        double iva = 0;
        if (compra.getItems() != null) {
            for (ItemCompra item : compra.getItems()) {
                if (item != null) {
                    subtotal += item.getSubtotal();
                    iva += item.getImpuestos();
                }
            }
        }
        double porcentajeDescuento = cliente != null ? cliente.calcularDescuento() : 0.0;
        double descuento = subtotal * porcentajeDescuento;
        double total = compra.getTotal() > 0
                ? compra.getTotal() : subtotal - descuento + iva;

        texto.append("\nSubtotal: ").append(String.format("$%,.2f", subtotal));
        texto.append("\nIVA: ").append(String.format("$%,.2f", iva));
        texto.append("\nDescuento Premium: -").append(String.format("$%,.2f", descuento));
        texto.append("\nTOTAL: ").append(String.format("$%,.2f", total));
        return texto.toString();
    }

    public JButton getBtnCerrar() {
        return btnCerrar;
    }

    public JFrame getPadre() {
        return padre;
    }
}
