package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import model.Cliente;
import model.ClientePremium;
import service.IServicioClientes;

/**
 * Ventana de gestion de clientes para el administrador: lista los clientes
 * registrados y permite eliminar el seleccionado.
 *
 * Solo depende de la interfaz IServicioClientes (no del controlador concreto),
 * y sigue el mismo patron de navegacion padre/hijo que las demas ventanas.
 */
public class VentanaClientes extends JFrame {

    private static final long serialVersionUID = 1L;

    private IServicioClientes controlador;
    private JFrame padre;

    private JTable tabla;
    private DefaultTableModel modeloTabla;

    public VentanaClientes(IServicioClientes controlador, JFrame padre) {
        this.controlador = controlador;
        this.padre = padre;

        setTitle("Gesti\u00f3n de Clientes");
        setSize(720, 440);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8, 8));

        add(new JScrollPane(crearTabla()), BorderLayout.CENTER);
        add(crearBotones(), BorderLayout.SOUTH);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (VentanaClientes.this.padre != null) {
                    VentanaClientes.this.padre.setVisible(true);
                }
            }
        });

        cargarTabla();
    }

    private JTable crearTabla() {
        modeloTabla = new DefaultTableModel(
                new Object[] { "Nombre", "Correo", "Direcci\u00f3n", "Tel\u00e9fono", "Tipo" }, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabla = new JTable(modeloTabla);
        return tabla;
    }

    private JPanel crearBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton btnAtras = new JButton("Atr\u00e1s");
        JButton btnEliminar = new JButton("Eliminar");

        btnAtras.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                volver();
            }
        });

        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                accionEliminar();
            }
        });

        panel.add(btnAtras);
        panel.add(btnEliminar);
        return panel;
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        for (Cliente cliente : controlador.listar()) {
            modeloTabla.addRow(new Object[] {
                    cliente.getNombreCompleto(),
                    cliente.getCorreo(),
                    cliente.getDireccion(),
                    cliente.getTelefono(),
                    (cliente instanceof ClientePremium) ? "Premium" : "Regular"
            });
        }
    }

    private void accionEliminar() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente de la tabla.");
            return;
        }

        String correo = (String) modeloTabla.getValueAt(fila, 1);
        int confirmacion = JOptionPane.showConfirmDialog(this,
                "\u00bfDesea eliminar el cliente " + correo + "?",
                "Confirmar eliminaci\u00f3n", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            if (controlador.eliminar(correo)) {
                JOptionPane.showMessageDialog(this, "Cliente eliminado correctamente.");
                cargarTabla();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo eliminar el cliente.");
            }
        }
    }

    private void volver() {
        dispose();
        if (padre != null) {
            padre.setVisible(true);
        }
    }
}
