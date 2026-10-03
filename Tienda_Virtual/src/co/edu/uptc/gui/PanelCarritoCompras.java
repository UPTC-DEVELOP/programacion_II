package co.edu.uptc.gui;

import co.edu.uptc.model.CarritoCompras;
import co.edu.uptc.model.ItemCarrito;
import co.edu.uptc.model.Libro;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class PanelCarritoCompras extends JPanel {
    private JTable tablaCarrito;
    private DefaultTableModel modeloTabla;

    private JComboBox<Libro> comboLibros;
    private JTextField txtCantidad;
    private JLabel lblSubtotal, lblIva, lblDescuento, lblTotal;
    private JButton btnAgregar, btnActualizar, btnEliminar, btnVaciar;

    public PanelCarritoCompras(ActionListener listener) {
        setLayout(new BorderLayout(10, 10));
        initFormulario(listener);
        initTabla();
        initTotales();
    }

    private void initFormulario(ActionListener listener) {
        JPanel panelForm = new JPanel(new GridLayout(2, 2, 8, 8));
        panelForm.setBorder(BorderFactory.createTitledBorder("Carrito de Compras (CRUD)"));

        comboLibros = new JComboBox<>();
        txtCantidad = new JTextField();

        panelForm.add(new JLabel("Libro:")); panelForm.add(comboLibros);
        panelForm.add(new JLabel("Cantidad:")); panelForm.add(txtCantidad);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnAgregar = new JButton("Agregar");
        btnAgregar.setActionCommand("AGREGAR_CARRITO");
        btnAgregar.addActionListener(listener);

        btnActualizar = new JButton("Actualizar cantidad");
        btnActualizar.setActionCommand("ACTUALIZAR_CARRITO");
        btnActualizar.addActionListener(listener);

        btnEliminar = new JButton("Eliminar");
        btnEliminar.setActionCommand("ELIMINAR_CARRITO");
        btnEliminar.addActionListener(listener);

        btnVaciar = new JButton("Vaciar carrito");
        btnVaciar.setActionCommand("VACIAR_CARRITO");
        btnVaciar.addActionListener(listener);

        panelBotones.add(btnAgregar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnVaciar);

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(panelForm, BorderLayout.CENTER);
        panelSuperior.add(panelBotones, BorderLayout.SOUTH);

        add(panelSuperior, BorderLayout.NORTH);
    }

    private void initTabla() {
        String[] columnas = {"ISBN", "Título", "Cantidad", "Precio Unit.", "Subtotal", "IVA"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaCarrito = new JTable(modeloTabla);
        tablaCarrito.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Al seleccionar una fila se carga su cantidad en el campo, listo para actualizarla
        tablaCarrito.getSelectionModel().addListSelectionListener(e -> {
            int fila = tablaCarrito.getSelectedRow();
            if (!e.getValueIsAdjusting() && fila != -1) {
                txtCantidad.setText(String.valueOf(modeloTabla.getValueAt(fila, 2)));
            }
        });

        add(new JScrollPane(tablaCarrito), BorderLayout.CENTER);
    }

    private void initTotales() {
        JPanel panelTotales = new JPanel(new GridLayout(2, 2, 8, 4));
        panelTotales.setBorder(BorderFactory.createTitledBorder("Resumen"));

        lblSubtotal = new JLabel("Subtotal: $0.00");
        lblIva = new JLabel("IVA: $0.00");
        lblDescuento = new JLabel("Descuento: $0.00");
        lblTotal = new JLabel("Total a pagar: $0.00");
        lblTotal.setFont(lblTotal.getFont().deriveFont(Font.BOLD));

        panelTotales.add(lblSubtotal);
        panelTotales.add(lblIva);
        panelTotales.add(lblDescuento);
        panelTotales.add(lblTotal);

        add(panelTotales, BorderLayout.SOUTH);
    }

    public void cargarLibros(List<Libro> libros) {
        comboLibros.removeAllItems();
        for (Libro l : libros) {
            comboLibros.addItem(l);
        }
    }

    public void cargarCarrito(CarritoCompras carrito) {
        modeloTabla.setRowCount(0);
        for (ItemCarrito item : carrito.getItems()) {
            Libro l = item.getLibro();
            modeloTabla.addRow(new Object[]{
                l.getIsbn(), l.getTitulo(), item.getCantidad(),
                String.format("%.2f", l.getPrecio()),
                String.format("%.2f", item.calcularSubtotal()),
                String.format("%.2f", item.calcularImpuesto())
            });
        }

        lblSubtotal.setText("Subtotal: $" + String.format("%.2f", carrito.calcularSubtotalTotal()));
        lblIva.setText("IVA: $" + String.format("%.2f", carrito.calcularTotalImpuestos()));
        lblDescuento.setText("Descuento: $" + String.format("%.2f", carrito.calcularDescuento()));
        lblTotal.setText("Total a pagar: $" + String.format("%.2f", carrito.calcularTotalPagar()));
    }

    public Libro getLibroSeleccionado() { return (Libro) comboLibros.getSelectedItem(); }

    public int getCantidad() { return Integer.parseInt(txtCantidad.getText().trim()); }

    // ISBN de la fila seleccionada en la tabla del carrito ("" si no hay selección)
    public String getIsbnItemSeleccionado() {
        int fila = tablaCarrito.getSelectedRow();
        if (fila == -1) {
            return "";
        }
        return (String) modeloTabla.getValueAt(fila, 0);
    }

    public void limpiarCampos() {
        txtCantidad.setText("");
        tablaCarrito.clearSelection();
    }
}