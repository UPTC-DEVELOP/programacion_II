package co.edu.uptc.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import co.edu.uptc.excepciones.ReglaNegocioException;
import co.edu.uptc.interfaces.IGestionCompra;
import co.edu.uptc.modelo.dto.CompraDtos.FacturaDto;
import co.edu.uptc.modelo.dto.CompraDtos.PagoDto;
/**
 * DIALOGO QUE MUESTRA EL RESUMEN DE LA COMPRA REALIZADA Y PERMITE VER LA FACTURA
 *  @author: Equipo de desarrollo grupo 7 UPTC
 *  @modificacion:Brayan Panqueva
 */
public class DialogoCompra extends JDialog {

    private static final long serialVersionUID = 1L;

    public DialogoCompra(java.awt.Frame padre, FacturaDto factura, IGestionCompra gestionCompra) {
        this(padre, factura, gestionCompra, f -> { }, new PagoDto("", "", "", 0, 0));
    }

    /*modificacion:Brayan Panqueva*/
    public DialogoCompra(java.awt.Frame padre, FacturaDto factura, IGestionCompra gestionCompra,
                         Consumer<FacturaDto> guardarFactura, PagoDto pago) {
        super(padre, "Compra finalizada", true);
        setLayout(new BorderLayout(10, 10));
        setSize(440, 260);
        setLocationRelativeTo(padre);
        NumberFormat moneda = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));
        JPanel centro = new JPanel(new java.awt.GridLayout(7, 1, 4, 4));
        centro.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 20, 5, 20));
        centro.add(new JLabel("Compra registrada correctamente."));
        centro.add(new JLabel("Número de venta: " + factura.idVenta()));
        centro.add(new JLabel("Subtotal: " + moneda.format(factura.subtotal())));
        centro.add(new JLabel("IVA: " + moneda.format(factura.impuestos())));
        centro.add(new JLabel("Total pagado: " + moneda.format(factura.total())));
        centro.add(new JLabel("Método: " + pago.metodo()));
        if ("EFECTIVO".equalsIgnoreCase(pago.metodo())) {
            centro.add(new JLabel("Cambio: " + moneda.format(pago.cambio())));
        } else {
            centro.add(new JLabel("Pago aprobado con tarjeta."));
        }
        add(centro, BorderLayout.CENTER);
        JPanel sur = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton ver = new JButton("Ver factura");
        ver.addActionListener(e -> {
            try {
                new DialogoFactura(padre, gestionCompra.consultarFactura(factura.idVenta()), guardarFactura).setVisible(true);
            } catch (ReglaNegocioException ex) {
                javax.swing.JOptionPane.showMessageDialog(this, ex.getMessage(), "Factura", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        });
        JButton cerrar = new JButton("Aceptar");
        cerrar.addActionListener(e -> { guardarFactura.accept(factura); dispose(); });
        sur.add(ver);
        sur.add(cerrar);
        add(sur, BorderLayout.SOUTH);
    }
}
