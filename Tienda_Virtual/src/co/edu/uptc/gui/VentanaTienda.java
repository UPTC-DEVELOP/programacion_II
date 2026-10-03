package co.edu.uptc.gui;

import co.edu.uptc.model.Libro;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class VentanaTienda extends JFrame {

    private JComboBox<Libro> comboLibros;
    private JTextField txtPrecioBase, txtStock, txtDescuento, txtIVA, txtCantidad;
    private JButton btnRegistrar, btnCancelarPedido;
    private JTable tablaHistorial;
    private DefaultTableModel modeloTabla;

    public VentanaTienda() {
        setTitle("Sistema de Gestión - Tienda Virtual");
        setSize(750, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // --- PANEL SUPERIOR: SELECCIÓN Y DATOS ---
        JPanel panelFormulario = new JPanel(new GridLayout(6, 2, 5, 5));
        
        comboLibros = new JComboBox<>();
        txtPrecioBase = new JTextField(); txtPrecioBase.setEditable(false);
        txtStock = new JTextField(); txtStock.setEditable(false);
        txtDescuento = new JTextField(); txtDescuento.setEditable(false);
        txtIVA = new JTextField(); txtIVA.setEditable(false);
        txtCantidad = new JTextField();

        panelFormulario.add(new JLabel(" Seleccionar Libro:"));
        panelFormulario.add(comboLibros);
        panelFormulario.add(new JLabel(" Precio Base ($):"));
        panelFormulario.add(txtPrecioBase);
        panelFormulario.add(new JLabel(" Stock Disponible:"));
        panelFormulario.add(txtStock);
        panelFormulario.add(new JLabel(" Descuento (%):"));
        panelFormulario.add(txtDescuento);
        panelFormulario.add(new JLabel(" IVA (%):"));
        panelFormulario.add(txtIVA);
        panelFormulario.add(new JLabel(" Cantidad a Comprar:"));
        panelFormulario.add(txtCantidad);

        add(panelFormulario, BorderLayout.NORTH);

        // --- PANEL CENTRAL: TABLA DE HISTORIAL ---
        String[] columnas = {"Libro", "Cantidad", "Precio Unit. Final", "Total Pagar"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaHistorial = new JTable(modeloTabla);
        add(new JScrollPane(tablaHistorial), BorderLayout.CENTER);

        // --- PANEL INFERIOR: BOTONES ---
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        btnRegistrar = new JButton("Registrar Pedido");
        btnCancelarPedido = new JButton("Cancelar Pedido Seleccionado");
        
        panelBotones.add(btnRegistrar);
        panelBotones.add(btnCancelarPedido);
        add(panelBotones, BorderLayout.SOUTH);

        // Cargar datos y configurar eventos
        cargarLibrosEjemplo();
        configurarEventos();
    }

    private void cargarLibrosEjemplo() {
        comboLibros.addItem(new Libro("111", "Cien Años de Soledad", "G. García M.", 1967, "Novela", "Sudamericana", 400, 60000, 19.0, 10.0, 15, "Físico"));
        comboLibros.addItem(new Libro("222", "El Principito", "A. de Saint-Exupéry", 1943, "Fábula", "Reynal & Hitchcock", 96, 35000, 19.0, 5.0, 8, "Físico"));
        comboLibros.addItem(new Libro("333", "Don Quijote de la Mancha", "M. de Cervantes", 1605, "Novela", "Francisco de Robles", 863, 80000, 19.0, 0.0, 5, "Físico"));
        
        actualizarCampos();
    }

    private void actualizarCampos() {
        Libro seleccionado = (Libro) comboLibros.getSelectedItem();
        if (seleccionado != null) {
            txtPrecioBase.setText(String.valueOf(seleccionado.getPrecio()));
            txtStock.setText(String.valueOf(seleccionado.getCantidadInventario()));
            txtDescuento.setText(String.valueOf(seleccionado.getPorcentajeDescuento()));
            txtIVA.setText(String.valueOf(seleccionado.getPorcentajeIva()));
        }
    }

    private void configurarEventos() {
        // 1. EVENTO CAMBIO DE SELECCIÓN (ItemListener)
        comboLibros.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    actualizarCampos();
                }
            }
        });

        // 2. EVENTO REGISTRAR PEDIDO
        btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarPedido();
            }
        });

        // 3. EVENTO CANCELAR PEDIDO (REVERSIÓN DE STOCK)
        btnCancelarPedido.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelarPedido();
            }
        });
    }

    private void registrarPedido() {
        Libro libroSeleccionado = (Libro) comboLibros.getSelectedItem();
        if (libroSeleccionado == null) return;

        try {
            int cantidad = Integer.parseInt(txtCantidad.getText().trim());

            if (cantidad <= 0) {
                JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor a cero.", "Atención", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (cantidad > libroSeleccionado.getCantidadInventario()) {
                JOptionPane.showMessageDialog(this, "Stock insuficiente. Solo quedan " + libroSeleccionado.getCantidadInventario() + " unidades.", "Error de Stock", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Descontar Stock
            libroSeleccionado.setCantidadInventario(libroSeleccionado.getCantidadInventario() - cantidad);

            // Calcular valores
            double precioFinalUnitario = libroSeleccionado.calcularPrecioFinal();
            double totalPagar = precioFinalUnitario * cantidad;

            // Agregar a la tabla
            Object[] fila = {
                libroSeleccionado,
                cantidad,
                String.format("%.2f", precioFinalUnitario),
                String.format("%.2f", totalPagar)
            };
            modeloTabla.addRow(fila);

            actualizarCampos();
            txtCantidad.setText("");
            JOptionPane.showMessageDialog(this, "Pedido registrado con éxito.", "Información", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese una cantidad numérica válida.", "Entrada Inválida", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cancelarPedido() {
        int filaSeleccionada = tablaHistorial.getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un pedido de la tabla para cancelarlo.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Libro libro = (Libro) modeloTabla.getValueAt(filaSeleccionada, 0);
        int cantidadComprada = (int) modeloTabla.getValueAt(filaSeleccionada, 1);

        // Restaurar stock
        libro.setCantidadInventario(libro.getCantidadInventario() + cantidadComprada);

        // Remover de la tabla
        modeloTabla.removeRow(filaSeleccionada);

        actualizarCampos();
        JOptionPane.showMessageDialog(this, "Pedido cancelado. El stock ha sido restaurado.", "Pedido Cancelado", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaTienda().setVisible(true));
    }
}