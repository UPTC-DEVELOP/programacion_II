package co.edu.uptc.tienda.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import co.edu.uptc.tienda.interfaz.IGestionCarrito;
import co.edu.uptc.tienda.modelo.ItemCarrito;
import co.edu.uptc.tienda.modelo.Libro;

public class PanelCarrito extends JPanel {

    private static final long serialVersionUID = 1L;

    public static final String ACCION_AGREGAR = "AGREGAR";
    public static final String ACCION_CAMBIAR = "CAMBIAR";
    public static final String ACCION_ELIMINAR = "ELIMINAR";

    private static final String[] COLUMNAS = { "ISBN", "Titulo", "Precio", "IVA %", "Cantidad", "Subtotal" };

    private JComboBox<Libro> cmbLibros;
    private JSpinner spnCantidad;
    private JButton btnAgregar;
    private JButton btnCambiar;
    private JButton btnEliminar;
    private JTable tablaItems;
    private DefaultTableModel modeloTabla;
    private JLabel lblSubtotal;
    private JLabel lblImpuestos;
    private JLabel lblTotal;

    public PanelCarrito(IGestionCarrito gestion, List<Libro> catalogo) {
        initComponents(catalogo);
        EventosCarrito eventos = new EventosCarrito(this, gestion);
        btnAgregar.setActionCommand(ACCION_AGREGAR);
        btnCambiar.setActionCommand(ACCION_CAMBIAR);
        btnEliminar.setActionCommand(ACCION_ELIMINAR);
        btnAgregar.addActionListener(eventos);
        btnCambiar.addActionListener(eventos);
        btnEliminar.addActionListener(eventos);
    }

    private void initComponents(List<Libro> catalogo) {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelNorte = new JPanel(new GridBagLayout());
        panelNorte.setBorder(BorderFactory.createTitledBorder("Agregar al carrito"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panelNorte.add(new JLabel("Libro:"), gbc);

        cmbLibros = new JComboBox<>(catalogo.toArray(new Libro[0]));
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panelNorte.add(cmbLibros, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        panelNorte.add(new JLabel("Cantidad:"), gbc);

        spnCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 999999, 1));
        gbc.gridx = 3;
        panelNorte.add(spnCantidad, gbc);

        btnAgregar = new JButton("Agregar");
        gbc.gridx = 4;
        panelNorte.add(btnAgregar, gbc);

        add(panelNorte, BorderLayout.NORTH);

        modeloTabla = new DefaultTableModel(COLUMNAS, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        tablaItems = new JTable(modeloTabla);
        tablaItems.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(tablaItems), BorderLayout.CENTER);

        JPanel panelSur = new JPanel(new BorderLayout(10, 10));

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panelBotones.setBorder(BorderFactory.createTitledBorder("Con el libro seleccionado (usa la cantidad de arriba)"));
        btnCambiar = new JButton("Cambiar cantidad");
        btnEliminar = new JButton("Eliminar");
        panelBotones.add(btnCambiar);
        panelBotones.add(btnEliminar);
        panelSur.add(panelBotones, BorderLayout.CENTER);

        JPanel panelTotales = new JPanel(new GridLayout(3, 2, 10, 5));
        panelTotales.setBorder(BorderFactory.createTitledBorder("Resumen"));
        lblSubtotal = new JLabel("$0.00", SwingConstants.RIGHT);
        lblImpuestos = new JLabel("$0.00", SwingConstants.RIGHT);
        lblTotal = new JLabel("$0.00", SwingConstants.RIGHT);
        panelTotales.add(new JLabel("Subtotal:"));
        panelTotales.add(lblSubtotal);
        panelTotales.add(new JLabel("Impuestos (IVA):"));
        panelTotales.add(lblImpuestos);
        panelTotales.add(new JLabel("Total:"));
        panelTotales.add(lblTotal);
        panelSur.add(panelTotales, BorderLayout.EAST);

        add(panelSur, BorderLayout.SOUTH);
    }

    public Libro getLibroSeleccionado() {
        return (Libro) cmbLibros.getSelectedItem();
    }

    public int getCantidad() {
        return (Integer) spnCantidad.getValue();
    }

    public String getIsbnSeleccionado() {
        int fila = tablaItems.getSelectedRow();
        if (fila < 0) {
            return null;
        }
        return (String) modeloTabla.getValueAt(fila, 0);
    }

    public void mostrarCarrito(List<ItemCarrito> items, double subtotal, double impuestos, double total) {
        modeloTabla.setRowCount(0);
        for (ItemCarrito item : items) {
            modeloTabla.addRow(new Object[] {
                    item.getLibro().getIsbn(),
                    item.getLibro().getTitulo(),
                    formatear(item.getLibro().getPrecioVenta()),
                    item.getLibro().getIvaPorcentaje(),
                    item.getCantidad(),
                    formatear(item.obtenerSubtotalItem()) });
        }
        lblSubtotal.setText(formatear(subtotal));
        lblImpuestos.setText(formatear(impuestos));
        lblTotal.setText(formatear(total));
    }

    private String formatear(double valor) {
        return String.format("$%,.2f", valor);
    }

    public void mostrarAlerta(String titulo, String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, titulo, JOptionPane.WARNING_MESSAGE);
    }

    public boolean confirmar(String mensaje) {
        return JOptionPane.showConfirmDialog(this, mensaje, "Confirmar",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }
}
