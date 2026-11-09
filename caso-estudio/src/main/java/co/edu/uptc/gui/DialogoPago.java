package co.edu.uptc.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import co.edu.uptc.modelo.dto.CompraDtos.PagoDto;
/*
 * DIALOGO QUE PERMITE AL USUARIO SELECCIONAR EL METODO DE PAGO Y DIGITAR LOS DATOS NECESARIOS PARA EL PAGO.
 * @author: Equipo de desarrollo grupo 7 UPTC
 * */
/*modificacion:Brayan Panqueva*/
public final class DialogoPago extends JDialog {

    private static final long serialVersionUID = 1L;
    private final double total;
    private final JComboBox<String> metodo = new JComboBox<>(new String[]{"EFECTIVO", "TARJETA"});
    private final JTextField monto = new JTextField();
    private final JTextField tarjeta = new JTextField();
    private final JPasswordField contrasena = new JPasswordField();
    private final JPanel tarjetas = new JPanel(new CardLayout());
    private PagoDto resultado;

    private DialogoPago(java.awt.Frame padre, double total) {
        super(padre, "Método de pago", true);
        this.total = total;
        setSize(440, 280);
        setLocationRelativeTo(padre);
        construir();
    }

    public static PagoDto mostrar(java.awt.Frame padre, double total) {
        DialogoPago dialogo = new DialogoPago(padre, total);
        dialogo.setVisible(true);
        return dialogo.resultado;
    }

    private void construir() {
        setLayout(new BorderLayout(10, 10));
        JPanel centro = new JPanel(new GridLayout(0, 2, 8, 8));
        centro.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 5, 15));
        centro.add(new JLabel("Total a pagar:"));
        centro.add(new JLabel(String.format("$ %.2f", total)));
        centro.add(new JLabel("Método:"));
        centro.add(metodo);
        JPanel efectivo = new JPanel(new GridLayout(1, 2, 8, 8));
        efectivo.add(new JLabel("Monto recibido:"));
        efectivo.add(monto);
        JPanel tarjetaPanel = new JPanel(new GridLayout(2, 2, 8, 8));
        tarjetaPanel.add(new JLabel("Tarjeta (16 dígitos):"));
        tarjetaPanel.add(tarjeta);
        tarjetaPanel.add(new JLabel("Contraseña segura:"));
        tarjetaPanel.add(contrasena);
        tarjetas.add(efectivo, "EFECTIVO");
        tarjetas.add(tarjetaPanel, "TARJETA");
        centro.add(new JLabel("Datos del pago:"));
        centro.add(tarjetas);
        add(centro, BorderLayout.CENTER);
        metodo.addActionListener(e -> ((CardLayout) tarjetas.getLayout()).show(tarjetas, (String) metodo.getSelectedItem()));
        JPanel sur = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton aceptar = new JButton("Aceptar");
        JButton cancelar = new JButton("Cancelar");
        aceptar.addActionListener(e -> aceptar());
        cancelar.addActionListener(e -> dispose());
        sur.add(aceptar);
        sur.add(cancelar);
        add(sur, BorderLayout.SOUTH);
    }

    private void aceptar() {
        String seleccionado = (String) metodo.getSelectedItem();
        if ("TARJETA".equals(seleccionado)) {
            resultado = new PagoDto("TARJETA", tarjeta.getText().trim(), new String(contrasena.getPassword()), total, 0);
        } else {
            double recibido;
            try {
                recibido = Double.parseDouble(monto.getText().trim().replace(',', '.'));
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Digite un monto válido.", "Pago", JOptionPane.ERROR_MESSAGE);
                return;
            }
            resultado = new PagoDto("EFECTIVO", "", "", recibido, 0);
        }
        dispose();
    }
}
