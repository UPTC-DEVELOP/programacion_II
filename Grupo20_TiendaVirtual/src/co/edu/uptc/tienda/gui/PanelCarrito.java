package co.edu.uptc.tienda.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import co.edu.uptc.tienda.modelo.Cliente;
import co.edu.uptc.tienda.modelo.ItemCarrito;
import co.edu.uptc.tienda.modelo.Libro;

public class PanelCarrito extends JPanel {

    private static final long serialVersionUID = 1L;

    private static final String[] COLUMNAS = { "ISBN", "Título", "Precio", "IVA %", "Cantidad", "Subtotal" };
    private static final String SIN_CLIENTE = "Sin cliente (sin descuento)";

    private JComboBox<Libro> cmbLibros;
    private JComboBox<String> cmbClientes;
    private List<Cliente> clientes = new ArrayList<>();
    private JSpinner spnCantidad;
    private JButton btnAgregar;
    private JButton btnCambiar;
    private JButton btnEliminar;
    private JButton btnVaciar;
    private JButton btnFinalizar;
    private JTable tablaItems;
    private DefaultTableModel modeloTabla;
    private JLabel lblSubtotal;
    private JLabel lblImpuestos;
    private JLabel lblDescuento;
    private JLabel lblTotal;

    public PanelCarrito() {
        initComponents();
    }

    public void setEventos(EventosCarrito eventos) {
        btnAgregar.setActionCommand(EventosCarrito.AGREGAR);
        btnCambiar.setActionCommand(EventosCarrito.CAMBIAR);
        btnEliminar.setActionCommand(EventosCarrito.ELIMINAR);
        btnVaciar.setActionCommand(EventosCarrito.VACIAR);
        cmbClientes.setActionCommand(EventosCarrito.CLIENTE);
        btnFinalizar.setActionCommand(EventosCarrito.FINALIZAR);
        btnAgregar.addActionListener(eventos);
        btnCambiar.addActionListener(eventos);
        btnEliminar.addActionListener(eventos);
        btnVaciar.addActionListener(eventos);
        cmbClientes.addActionListener(eventos);
        btnFinalizar.addActionListener(eventos);
    }

    private void initComponents() {
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

        cmbLibros = new JComboBox<>();
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

        btnAgregar = new JButton(EventosCarrito.AGREGAR);
        gbc.gridx = 4;
        panelNorte.add(btnAgregar, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panelNorte.add(new JLabel("Cliente:"), gbc);

        cmbClientes = new JComboBox<>(new String[] { SIN_CLIENTE });
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panelNorte.add(cmbClientes, gbc);

        add(panelNorte, BorderLayout.NORTH);

        modeloTabla = new DefaultTableModel(COLUMNAS, 0);
        tablaItems = new JTable(modeloTabla);
        tablaItems.setDefaultEditor(Object.class, null);
        tablaItems.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(tablaItems), BorderLayout.CENTER);

        JPanel panelSur = new JPanel(new BorderLayout(10, 10));

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panelBotones.setBorder(BorderFactory.createTitledBorder("Con el libro seleccionado (usa la cantidad de arriba)"));
        btnCambiar = new JButton(EventosCarrito.CAMBIAR);
        btnEliminar = new JButton(EventosCarrito.ELIMINAR);
        btnVaciar = new JButton(EventosCarrito.VACIAR);
        panelBotones.add(btnCambiar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnVaciar);
        panelSur.add(panelBotones, BorderLayout.CENTER);

        JPanel panelTotales = new JPanel(new GridLayout(4, 2, 10, 5));
        panelTotales.setBorder(BorderFactory.createTitledBorder("Resumen"));
        lblSubtotal = new JLabel("$0.00", SwingConstants.RIGHT);
        lblImpuestos = new JLabel("$0.00", SwingConstants.RIGHT);
        lblDescuento = new JLabel("$0.00", SwingConstants.RIGHT);
        lblTotal = new JLabel("$0.00", SwingConstants.RIGHT);
        panelTotales.add(new JLabel("Subtotal:"));
        panelTotales.add(lblSubtotal);
        panelTotales.add(new JLabel("Impuestos (IVA):"));
        panelTotales.add(lblImpuestos);
        panelTotales.add(new JLabel("Descuento:"));
        panelTotales.add(lblDescuento);
        panelTotales.add(new JLabel("Total:"));
        panelTotales.add(lblTotal);

        JPanel panelCompra = new JPanel(new BorderLayout(5, 5));
        btnFinalizar = new JButton(EventosCarrito.FINALIZAR);
        panelCompra.add(panelTotales, BorderLayout.CENTER);
        panelCompra.add(btnFinalizar, BorderLayout.SOUTH);
        panelSur.add(panelCompra, BorderLayout.EAST);

        add(panelSur, BorderLayout.SOUTH);
    }

    public Libro getLibroSeleccionado() {
        return (Libro) cmbLibros.getSelectedItem();
    }

    public int getCantidad() {
        return (Integer) spnCantidad.getValue();
    }

    public Cliente getClienteSeleccionado() {
        int indice = cmbClientes.getSelectedIndex();
        if (indice <= 0) {
            return null;
        }
        return clientes.get(indice - 1);
    }

    public void cargarCatalogo(List<Libro> catalogo) {
        Libro anterior = getLibroSeleccionado();
        cmbLibros.setModel(new DefaultComboBoxModel<>(catalogo.toArray(new Libro[0])));
        if (anterior != null && catalogo.contains(anterior)) {
            cmbLibros.setSelectedItem(anterior);
        }
    }

    public void cargarClientes(List<Cliente> clientes, String idSeleccionado) {
        this.clientes = new ArrayList<>(clientes);
        DefaultComboBoxModel<String> modelo = new DefaultComboBoxModel<>();
        modelo.addElement(SIN_CLIENTE);
        int indice = 0;
        for (int i = 0; i < clientes.size(); i++) {
            Cliente cliente = clientes.get(i);
            modelo.addElement(cliente.getNombreCompleto() + " (" + cliente.getId() + ")");
            if (cliente.getId().equals(idSeleccionado)) {
                indice = i + 1;
            }
        }
        cmbClientes.setModel(modelo);
        cmbClientes.setSelectedIndex(indice);
    }

    public String getIsbnSeleccionado() {
        int fila = tablaItems.getSelectedRow();
        if (fila < 0) {
            return null;
        }
        return (String) modeloTabla.getValueAt(fila, 0);
    }

    public void mostrarCarrito(List<ItemCarrito> items, double subtotal, double impuestos, double descuento,
            double porcentajeDescuento, double total) {
        modeloTabla.setRowCount(0);
        for (ItemCarrito item : items) {
            modeloTabla.addRow(new Object[] {
                    item.getLibro().getIsbn(),
                    item.getLibro().getTitulo(),
                    formatear(item.getLibro().getPrecio()),
                    String.format("%.0f", item.getLibro().getIvaPorcentaje()),
                    item.getCantidad(),
                    formatear(item.obtenerSubtotalItem()) });
        }
        lblSubtotal.setText(formatear(subtotal));
        lblImpuestos.setText(formatear(impuestos));
        lblDescuento.setText(porcentajeDescuento > 0
                ? "-" + formatear(descuento) + " (" + String.format("%.0f", porcentajeDescuento) + "%)"
                : formatear(0));
        lblTotal.setText(formatear(total));
    }

    private String formatear(double valor) {
        return String.format("$%,.2f", valor);
    }

    public void mostrarAlerta(String titulo, String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, titulo, JOptionPane.WARNING_MESSAGE);
    }

    public String pedirCorreo(String nombreCliente) {
        return JOptionPane.showInputDialog(this, "Para pagar, " + nombreCliente
                + " debe iniciar sesión.\nEscriba su correo electrónico:", "Iniciar sesión",
                JOptionPane.QUESTION_MESSAGE);
    }

    public String pedirMetodoPago(String resumen, String[] metodos) {
        JPanel contenido = new JPanel(new BorderLayout(5, 10));
        contenido.add(crearTexto(resumen, 360, 220), BorderLayout.CENTER);
        JPanel panelPago = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        JComboBox<String> cmbMetodos = new JComboBox<>(metodos);
        panelPago.add(new JLabel("Método de pago (simulado):"));
        panelPago.add(cmbMetodos);
        contenido.add(panelPago, BorderLayout.SOUTH);
        String[] opciones = { "Confirmar compra", "Cancelar" };
        int respuesta = JOptionPane.showOptionDialog(this, contenido, "Resumen del pedido",
                JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, opciones, opciones[0]);
        return respuesta == 0 ? (String) cmbMetodos.getSelectedItem() : null;
    }

    public void mostrarRecibo(String recibo) {
        JOptionPane.showMessageDialog(this, crearTexto(recibo, 560, 330), "Recibo de compra",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private JScrollPane crearTexto(String texto, int ancho, int alto) {
        JTextArea area = new JTextArea(texto);
        area.setEditable(false);
        area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(ancho, alto));
        return scroll;
    }

    public boolean confirmar(String mensaje) {
        return JOptionPane.showConfirmDialog(this, mensaje, "Confirmar",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }
}
