package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import model.Cliente;
import model.Compra;
import model.ItemCompra;
import service.IServicioCompras;

public class VentanaHistorialCompras extends JFrame {

    private static final long serialVersionUID = 1L;

    private final IServicioCompras controladorCompras;
    private final Cliente cliente;
    private final JFrame padre;

    private JTable tabla;
    private DefaultTableModel modeloTabla;

    public VentanaHistorialCompras(IServicioCompras controladorCompras,
            Cliente cliente, JFrame padre) {
        this.controladorCompras = controladorCompras;
        this.cliente = cliente;
        this.padre = padre;

        setTitle("Historial de Compras");
        setSize(700, 420);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8, 8));

        add(new JScrollPane(crearTabla()), BorderLayout.CENTER);
        add(crearBoton(), BorderLayout.SOUTH);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (VentanaHistorialCompras.this.padre != null) {
                    VentanaHistorialCompras.this.padre.setVisible(true);
                }
            }
        });

        cargarTabla();
    }

    private JTable crearTabla() {
        modeloTabla = new DefaultTableModel(
                new Object[] { "ID", "Fecha", "Método de pago", "Artículos", "Total" }, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabla = new JTable(modeloTabla);
        return tabla;
    }

    private JPanel crearBoton() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnAtras = new JButton("Atrás");
        btnAtras.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                volver();
            }
        });
        panel.add(btnAtras);
        return panel;
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        if (cliente == null) {
            return;
        }
        List<Compra> compras = controladorCompras.listarPorCliente(cliente.getCorreo());
        for (Compra compra : compras) {
            int articulos = 0;
            if (compra.getItems() != null) {
                for (ItemCompra item : compra.getItems()) {
                    articulos += item.getCantidad();
                }
            }
            double total = compra.getTotal() > 0
                    ? compra.getTotal() : compra.calcularTotal();
            modeloTabla.addRow(new Object[] {
                    compra.getId(),
                    compra.getFecha(),
                    compra.getMetodoPago(),
                    articulos,
                    String.format("$%,.2f", total)
            });
        }
    }

    private void volver() {
        dispose();
        if (padre != null) {
            padre.setVisible(true);
        }
    }
}
