package co.edu.uptc.gui;

import co.edu.uptc.negocio.Libro;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class PanelProducto extends JPanel {

    private static final long serialVersionUID = 1L;

    private JComboBox<Libro> cmbProductos;
    private DefaultComboBoxModel<Libro> modeloProductos;
    private JLabel lblPrecioBase;
    private JLabel lblStock;
    private JLabel lblDescuento;
    private JLabel lblIva;
    private JLabel lblPrecioFinal;
    private JTextField txtCantidad;
    private JButton btnRegistrar;
    private JLabel lblTotal;

    public PanelProducto() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(0, 8));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Nuevo Pedido"),
                BorderFactory.createEmptyBorder(4, 8, 8, 8)));

        modeloProductos = new DefaultComboBoxModel<>();
        cmbProductos = new JComboBox<>(modeloProductos);
        cmbProductos.setActionCommand(Comandos.SELECCION_PRODUCTO);
       
        cmbProductos.setRenderer(new DefaultListCellRenderer() {
            private static final long serialVersionUID = 1L;

            @Override
            public Component getListCellRendererComponent(JList<?> lista, Object valor, int indice,
                                                          boolean seleccionado, boolean foco) {
                super.getListCellRendererComponent(lista, valor, indice, seleccionado, foco);
                if (valor instanceof Libro) {
                    Libro l = (Libro) valor;
                    setText(l.getTitulo() + " (" + l.getFormato().getEtiqueta() + ")");
                }
                return this;
            }
        });
        JPanel panelSelector = new JPanel(new BorderLayout(8, 0));
        panelSelector.add(new JLabel("Producto:"), BorderLayout.WEST);
        panelSelector.add(cmbProductos, BorderLayout.CENTER);

        // ---- Datos del producto seleccionado (etiquetas) ----
        lblPrecioBase = new JLabel("-");
        lblStock = new JLabel("-");
        lblDescuento = new JLabel("-");
        lblIva = new JLabel("-");
        lblPrecioFinal = new JLabel("-");
        JPanel panelDatos = new JPanel(new GridLayout(0, 4, 8, 6));
        panelDatos.add(new JLabel("Precio Base:"));
        panelDatos.add(lblPrecioBase);
        panelDatos.add(new JLabel("Stock Disponible:"));
        panelDatos.add(lblStock);
        panelDatos.add(new JLabel("Descuento:"));
        panelDatos.add(lblDescuento);
        panelDatos.add(new JLabel("IVA:"));
        panelDatos.add(lblIva);
        panelDatos.add(new JLabel("Precio final unitario:"));
        panelDatos.add(lblPrecioFinal);

        // ---- Cantidad, botón y total ----
        txtCantidad = new JTextField(8);
        btnRegistrar = new JButton("Registrar Pedido");
        btnRegistrar.setActionCommand(Comandos.REGISTRAR_PEDIDO);
        lblTotal = new JLabel("Total a pagar: -");
        JPanel panelAccion = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        panelAccion.add(new JLabel("Cantidad:"));
        panelAccion.add(txtCantidad);
        panelAccion.add(btnRegistrar);
        panelAccion.add(lblTotal);

        JPanel panelCentro = new JPanel(new BorderLayout(0, 8));
        panelCentro.add(panelDatos, BorderLayout.NORTH);
        panelCentro.add(panelAccion, BorderLayout.CENTER);

        add(panelSelector, BorderLayout.NORTH);
        add(panelCentro, BorderLayout.CENTER);
    }

    // ===================== Eventos =====================

    public void agregarListener(ActionListener listener) {
        cmbProductos.addActionListener(listener);
        btnRegistrar.addActionListener(listener);
        txtCantidad.addActionListener(listener); 
        txtCantidad.setActionCommand(Comandos.REGISTRAR_PEDIDO);
    }

    // ===================== Lectura =====================

    public Libro getProductoSeleccionado() {
        return (Libro) cmbProductos.getSelectedItem();
    }

    public String getCantidad() {
        return txtCantidad.getText().trim();
    }

    // ===================== Escritura visual =====================

    public void cargarProductos(List<Libro> productos) {
        Libro anterior = getProductoSeleccionado();
        String isbnAnterior = anterior != null ? anterior.getIsbn() : null;

        modeloProductos.removeAllElements();
        Libro aSeleccionar = null;
        for (Libro libro : productos) {
            modeloProductos.addElement(libro);
            if (libro.getIsbn().equals(isbnAnterior)) {
                aSeleccionar = libro;
            }
        }
        if (aSeleccionar != null) {
            cmbProductos.setSelectedItem(aSeleccionar);
        } else if (modeloProductos.getSize() > 0) {
            cmbProductos.setSelectedIndex(0);
        }
    }

    public void mostrarProducto(Libro libro) {
        if (libro == null) {
            lblPrecioBase.setText("-");
            lblStock.setText("-");
            lblDescuento.setText("-");
            lblIva.setText("-");
            lblPrecioFinal.setText("-");
            return;
        }
        lblPrecioBase.setText(UtilidadesGUI.formatearMoneda(libro.getPrecioBase()));
        lblStock.setText(String.valueOf(libro.getStock()));
        lblDescuento.setText(UtilidadesGUI.formatearPorcentaje(libro.getPorcentajeDescuento()));
        lblIva.setText(UtilidadesGUI.formatearPorcentaje(libro.getImpuestoIVA()));
        lblPrecioFinal.setText(UtilidadesGUI.formatearMoneda(libro.calcularPrecioFinal()));
    }

    public void mostrarTotal(double total) {
        lblTotal.setText("Total a pagar: " + UtilidadesGUI.formatearMoneda(total));
    }

    public void limpiarCantidad() {
        txtCantidad.setText("");
        txtCantidad.requestFocusInWindow();
    }
}
