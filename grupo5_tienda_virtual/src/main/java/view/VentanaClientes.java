package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import service.IServicioClientes;

/**
 * Ventana de gestion de clientes para el administrador: lista los clientes
 * registrados y permite eliminar el seleccionado.
 */
public class VentanaClientes extends JFrame {

    private static final long serialVersionUID = 1L;

    private final EventosClientes eventos;

    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JButton btnAtras;
    private JButton btnEliminar;

    public VentanaClientes(IServicioClientes controlador, JFrame padre) {
        this.eventos = new EventosClientes(this, controlador);

        setTitle("Gesti\u00f3n de Clientes");
        setSize(720, 440);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8, 8));

        add(new JScrollPane(crearTabla()), BorderLayout.CENTER);
        add(crearBotones(), BorderLayout.SOUTH);

        eventos.registrarEventos(tabla, modeloTabla, btnAtras, btnEliminar, padre);
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

        btnAtras = new JButton("Atr\u00e1s");
        btnEliminar = new JButton("Eliminar");

        panel.add(btnAtras);
        panel.add(btnEliminar);
        return panel;
    }
}